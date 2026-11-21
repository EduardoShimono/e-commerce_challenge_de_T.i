package br.com.arenagames.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Carrinho guardado na sessao do usuario.
 * Guarda apenas o id do produto e a quantidade; preco e estoque
 * sao sempre consultados no banco para evitar valores desatualizados.
 */
public class Carrinho implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<Integer, Integer> itens = new LinkedHashMap<>();

    public void adicionar(int produtoId, int quantidade) {
        itens.merge(produtoId, quantidade, Integer::sum);
    }

    public void alterar(int produtoId, int quantidade) {
        if (quantidade <= 0) {
            itens.remove(produtoId);
        } else {
            itens.put(produtoId, quantidade);
        }
    }

    public void remover(int produtoId) {
        itens.remove(produtoId);
    }

    public int getQuantidade(int produtoId) {
        return itens.getOrDefault(produtoId, 0);
    }

    public Map<Integer, Integer> getItens() {
        return itens;
    }

    public int getTotalItens() {
        int total = 0;
        for (int q : itens.values()) {
            total += q;
        }
        return total;
    }

    public boolean isVazio() {
        return itens.isEmpty();
    }

    public void limpar() {
        itens.clear();
    }
}
