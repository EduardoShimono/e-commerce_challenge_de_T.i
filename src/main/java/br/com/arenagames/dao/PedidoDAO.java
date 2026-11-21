package br.com.arenagames.dao;

import br.com.arenagames.config.Conexao;
import br.com.arenagames.model.Carrinho;
import br.com.arenagames.model.ItemPedido;
import br.com.arenagames.model.Pedido;
import br.com.arenagames.util.EstoqueInsuficienteException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoDAO {

    /**
     * Finaliza a compra dentro de uma unica transacao:
     *   1. confere preco e estoque de cada produto (com bloqueio de linha)
     *   2. cria o pedido
     *   3. grava os itens com o preco praticado
     *   4. baixa o estoque
     * Se qualquer passo falhar, tudo e desfeito.
     *
     * @return id do pedido criado
     */
    public int finalizar(int usuarioId, String formaPagamento, Carrinho carrinho)
            throws SQLException, EstoqueInsuficienteException {

        String sqlProduto = "SELECT nome, preco, estoque FROM produtos WHERE id = ? AND ativo = 1 FOR UPDATE";
        String sqlPedido = "INSERT INTO pedidos (usuario_id, forma_pagamento, total) VALUES (?, ?, ?)";
        String sqlItem = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        String sqlEstoque = "UPDATE produtos SET estoque = estoque - ? WHERE id = ? AND estoque >= ?";

        try (Connection con = Conexao.abrir()) {
            con.setAutoCommit(false);
            try {
                Map<Integer, BigDecimal> precos = new LinkedHashMap<>();
                BigDecimal total = BigDecimal.ZERO;

                try (PreparedStatement ps = con.prepareStatement(sqlProduto)) {
                    for (Map.Entry<Integer, Integer> item : carrinho.getItens().entrySet()) {
                        ps.setInt(1, item.getKey());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                throw new EstoqueInsuficienteException("Um dos produtos do carrinho não está mais disponível.");
                            }
                            int estoque = rs.getInt("estoque");
                            if (item.getValue() > estoque) {
                                throw new EstoqueInsuficienteException("Estoque insuficiente para \""
                                        + rs.getString("nome") + "\". Disponível: " + estoque + ".");
                            }
                            BigDecimal preco = rs.getBigDecimal("preco");
                            precos.put(item.getKey(), preco);
                            total = total.add(preco.multiply(BigDecimal.valueOf(item.getValue())));
                        }
                    }
                }

                int pedidoId;
                try (PreparedStatement ps = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, usuarioId);
                    ps.setString(2, formaPagamento);
                    ps.setBigDecimal(3, total);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        pedidoId = rs.getInt(1);
                    }
                }

                try (PreparedStatement psItem = con.prepareStatement(sqlItem);
                     PreparedStatement psEstoque = con.prepareStatement(sqlEstoque)) {
                    for (Map.Entry<Integer, Integer> item : carrinho.getItens().entrySet()) {
                        int produtoId = item.getKey();
                        int qtd = item.getValue();

                        psItem.setInt(1, pedidoId);
                        psItem.setInt(2, produtoId);
                        psItem.setInt(3, qtd);
                        psItem.setBigDecimal(4, precos.get(produtoId));
                        psItem.executeUpdate();

                        psEstoque.setInt(1, qtd);
                        psEstoque.setInt(2, produtoId);
                        psEstoque.setInt(3, qtd);
                        if (psEstoque.executeUpdate() == 0) {
                            throw new EstoqueInsuficienteException("Estoque insuficiente para concluir o pedido.");
                        }
                    }
                }

                con.commit();
                return pedidoId;
            } catch (SQLException | EstoqueInsuficienteException | RuntimeException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public List<Pedido> listarTodos() throws SQLException {
        String sql = "SELECT p.*, u.nome AS cliente_nome, u.email AS cliente_email "
                + "FROM pedidos p INNER JOIN usuarios u ON u.id = p.usuario_id "
                + "ORDER BY p.data_pedido DESC, p.id DESC";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            List<Pedido> pedidos = montarPedidos(ps);
            carregarItens(con, pedidos);
            return pedidos;
        }
    }

    public List<Pedido> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT p.*, u.nome AS cliente_nome, u.email AS cliente_email "
                + "FROM pedidos p INNER JOIN usuarios u ON u.id = p.usuario_id "
                + "WHERE p.usuario_id = ? ORDER BY p.data_pedido DESC, p.id DESC";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            List<Pedido> pedidos = montarPedidos(ps);
            carregarItens(con, pedidos);
            return pedidos;
        }
    }

    public Pedido buscarDoUsuario(int pedidoId, int usuarioId) throws SQLException {
        String sql = "SELECT p.*, u.nome AS cliente_nome, u.email AS cliente_email "
                + "FROM pedidos p INNER JOIN usuarios u ON u.id = p.usuario_id "
                + "WHERE p.id = ? AND p.usuario_id = ?";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            ps.setInt(2, usuarioId);
            List<Pedido> pedidos = montarPedidos(ps);
            carregarItens(con, pedidos);
            return pedidos.isEmpty() ? null : pedidos.get(0);
        }
    }

    private List<Pedido> montarPedidos(PreparedStatement ps) throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));
                p.setUsuarioId(rs.getInt("usuario_id"));
                p.setClienteNome(rs.getString("cliente_nome"));
                p.setClienteEmail(rs.getString("cliente_email"));
                Timestamp ts = rs.getTimestamp("data_pedido");
                p.setDataPedido(ts == null ? null : ts.toLocalDateTime());
                p.setFormaPagamento(rs.getString("forma_pagamento"));
                p.setTotal(rs.getBigDecimal("total"));
                p.setStatus(rs.getString("status"));
                lista.add(p);
            }
        }
        return lista;
    }

    private void carregarItens(Connection con, List<Pedido> pedidos) throws SQLException {
        String sql = "SELECT i.id, i.pedido_id, i.produto_id, i.quantidade, i.preco_unitario, pr.nome AS produto_nome "
                + "FROM itens_pedido i INNER JOIN produtos pr ON pr.id = i.produto_id "
                + "WHERE i.pedido_id = ? ORDER BY i.id";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Pedido pedido : pedidos) {
                ps.setInt(1, pedido.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ItemPedido item = new ItemPedido();
                        item.setId(rs.getInt("id"));
                        item.setPedidoId(rs.getInt("pedido_id"));
                        item.setProdutoId(rs.getInt("produto_id"));
                        item.setProdutoNome(rs.getString("produto_nome"));
                        item.setQuantidade(rs.getInt("quantidade"));
                        item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
                        pedido.getItens().add(item);
                    }
                }
            }
        }
    }
}
