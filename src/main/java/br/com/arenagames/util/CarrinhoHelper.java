package br.com.arenagames.util;

import br.com.arenagames.dao.ProdutoDAO;
import br.com.arenagames.model.Carrinho;
import br.com.arenagames.model.ItemCarrinho;
import br.com.arenagames.model.Produto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CarrinhoHelper {

    private CarrinhoHelper() {
    }

    /**
     * Monta as linhas do carrinho com os dados atuais do banco.
     * Produtos desativados sao removidos e quantidades acima do estoque sao ajustadas.
     *
     * @return lista de avisos gerados durante o ajuste (pode ser vazia)
     */
    public static List<String> sincronizar(Carrinho carrinho, List<ItemCarrinho> destino) throws SQLException {
        ProdutoDAO dao = new ProdutoDAO();
        List<String> avisos = new ArrayList<>();

        for (Map.Entry<Integer, Integer> e : new ArrayList<>(carrinho.getItens().entrySet())) {
            Produto p = dao.buscarPorId(e.getKey());
            if (p == null || !p.isAtivo() || p.getEstoque() == 0) {
                carrinho.remover(e.getKey());
                avisos.add((p == null ? "Um produto" : "\"" + p.getNome() + "\"")
                        + " foi removido do carrinho por estar indisponível.");
                continue;
            }
            int qtd = e.getValue();
            if (qtd > p.getEstoque()) {
                qtd = p.getEstoque();
                carrinho.alterar(p.getId(), qtd);
                avisos.add("A quantidade de \"" + p.getNome() + "\" foi ajustada para " + qtd
                        + " (estoque disponível).");
            }
            destino.add(new ItemCarrinho(p, qtd));
        }
        return avisos;
    }

    public static BigDecimal total(List<ItemCarrinho> itens) {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrinho i : itens) {
            total = total.add(i.getSubtotal());
        }
        return total;
    }
}
