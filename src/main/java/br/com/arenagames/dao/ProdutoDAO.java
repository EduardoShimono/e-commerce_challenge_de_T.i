package br.com.arenagames.dao;

import br.com.arenagames.config.Conexao;
import br.com.arenagames.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private static final String SELECT_BASE =
            "SELECT p.id, p.categoria_id, c.nome AS categoria_nome, p.nome, p.descricao, "
            + "p.preco, p.estoque, p.imagem, p.ativo "
            + "FROM produtos p INNER JOIN categorias c ON c.id = p.categoria_id ";

    /**
     * Lista os produtos ativos da vitrine, com filtro opcional por texto e categoria.
     * Os filtros sao sempre passados como parametros do PreparedStatement.
     */
    public List<Produto> listarVitrine(String busca, Integer categoriaId) throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT_BASE).append("WHERE p.ativo = 1 ");
        List<Object> params = new ArrayList<>();

        if (busca != null && !busca.isBlank()) {
            sql.append("AND (p.nome LIKE ? OR p.descricao LIKE ?) ");
            String termo = "%" + busca.trim() + "%";
            params.add(termo);
            params.add(termo);
        }
        if (categoriaId != null) {
            sql.append("AND p.categoria_id = ? ");
            params.add(categoriaId);
        }
        sql.append("ORDER BY p.nome");

        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            return montarLista(ps);
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY p.id DESC";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return montarLista(ps);
        }
    }

    public Produto buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.id = ?";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            List<Produto> lista = montarLista(ps);
            return lista.isEmpty() ? null : lista.get(0);
        }
    }

    public void inserir(Produto p) throws SQLException {
        String sql = "INSERT INTO produtos (categoria_id, nome, descricao, preco, estoque, imagem, ativo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, p);
            ps.executeUpdate();
        }
    }

    public void atualizar(Produto p) throws SQLException {
        String sql = "UPDATE produtos SET categoria_id = ?, nome = ?, descricao = ?, preco = ?, "
                + "estoque = ?, imagem = ?, ativo = ? WHERE id = ?";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, p);
            ps.setInt(8, p.getId());
            ps.executeUpdate();
        }
    }

    public void alterarAtivo(int id, boolean ativo) throws SQLException {
        String sql = "UPDATE produtos SET ativo = ? WHERE id = ?";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, ativo);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * Exclui o produto. Se ele ja fizer parte de algum pedido, a chave estrangeira
     * impede a exclusao; nesse caso o produto e apenas desativado.
     *
     * @return true se foi excluido, false se foi desativado
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            alterarAtivo(id, false);
            return false;
        }
    }

    private void preencher(PreparedStatement ps, Produto p) throws SQLException {
        ps.setInt(1, p.getCategoriaId());
        ps.setString(2, p.getNome());
        ps.setString(3, p.getDescricao());
        ps.setBigDecimal(4, p.getPreco());
        ps.setInt(5, p.getEstoque());
        ps.setString(6, p.getImagem());
        ps.setBoolean(7, p.isAtivo());
    }

    private List<Produto> montarLista(PreparedStatement ps) throws SQLException {
        List<Produto> lista = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setCategoriaId(rs.getInt("categoria_id"));
                p.setCategoriaNome(rs.getString("categoria_nome"));
                p.setNome(rs.getString("nome"));
                p.setDescricao(rs.getString("descricao"));
                p.setPreco(rs.getBigDecimal("preco"));
                p.setEstoque(rs.getInt("estoque"));
                p.setImagem(rs.getString("imagem"));
                p.setAtivo(rs.getBoolean("ativo"));
                lista.add(p);
            }
        }
        return lista;
    }
}
