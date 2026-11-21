package br.com.arenagames.servlet;

import br.com.arenagames.dao.ProdutoDAO;
import br.com.arenagames.model.Carrinho;
import br.com.arenagames.model.ItemCarrinho;
import br.com.arenagames.model.Produto;
import br.com.arenagames.util.CarrinhoHelper;
import br.com.arenagames.util.Web;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/carrinho")
public class CarrinhoServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Carrinho carrinho = Web.carrinho(req);
        List<ItemCarrinho> itens = new ArrayList<>();
        try {
            List<String> avisos = CarrinhoHelper.sincronizar(carrinho, itens);
            req.setAttribute("avisos", avisos);
        } catch (SQLException e) {
            throw new ServletException("Erro ao carregar o carrinho", e);
        }
        req.setAttribute("itens", itens);
        req.setAttribute("total", CarrinhoHelper.total(itens));
        Web.render(req, resp, "carrinho");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        Integer produtoId = Web.inteiro(req.getParameter("produtoId"));
        Carrinho carrinho = Web.carrinho(req);

        try {
            if ("limpar".equals(acao)) {
                carrinho.limpar();
                Web.flash(req, Web.MENSAGEM, "Carrinho esvaziado.");
            } else if (produtoId == null) {
                Web.flash(req, Web.ERRO, "Produto inválido.");
            } else if ("remover".equals(acao)) {
                carrinho.remover(produtoId);
                Web.flash(req, Web.MENSAGEM, "Item removido do carrinho.");
            } else if ("adicionar".equals(acao)) {
                adicionar(req, carrinho, produtoId);
            } else if ("alterar".equals(acao)) {
                alterar(req, carrinho, produtoId);
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao atualizar o carrinho", e);
        }

        Web.redirect(req, resp, "/carrinho");
    }

    private void adicionar(HttpServletRequest req, Carrinho carrinho, int produtoId) throws SQLException {
        int quantidade = Web.inteiro(req.getParameter("quantidade"), 1);
        Produto p = produtoDAO.buscarPorId(produtoId);

        if (p == null || !p.isDisponivel()) {
            Web.flash(req, Web.ERRO, "Produto indisponível no momento.");
            return;
        }
        if (quantidade < 1) {
            Web.flash(req, Web.ERRO, "Informe uma quantidade válida.");
            return;
        }

        int jaNoCarrinho = carrinho.getQuantidade(produtoId);
        if (jaNoCarrinho + quantidade > p.getEstoque()) {
            Web.flash(req, Web.ERRO, "Não há estoque suficiente de \"" + p.getNome() + "\". Disponível: "
                    + p.getEstoque() + " (você já tem " + jaNoCarrinho + " no carrinho).");
            return;
        }

        carrinho.adicionar(produtoId, quantidade);
        Web.flash(req, Web.MENSAGEM, "\"" + p.getNome() + "\" adicionado ao carrinho.");
    }

    private void alterar(HttpServletRequest req, Carrinho carrinho, int produtoId) throws SQLException {
        Integer quantidade = Web.inteiro(req.getParameter("quantidade"));
        if (quantidade == null) {
            Web.flash(req, Web.ERRO, "Informe uma quantidade válida.");
            return;
        }
        if (quantidade <= 0) {
            carrinho.remover(produtoId);
            Web.flash(req, Web.MENSAGEM, "Item removido do carrinho.");
            return;
        }

        Produto p = produtoDAO.buscarPorId(produtoId);
        if (p == null || !p.isDisponivel()) {
            carrinho.remover(produtoId);
            Web.flash(req, Web.ERRO, "Produto indisponível, item removido do carrinho.");
            return;
        }
        if (quantidade > p.getEstoque()) {
            Web.flash(req, Web.ERRO, "Quantidade maior que o estoque de \"" + p.getNome()
                    + "\". Máximo disponível: " + p.getEstoque() + ".");
            return;
        }

        carrinho.alterar(produtoId, quantidade);
        Web.flash(req, Web.MENSAGEM, "Quantidade atualizada.");
    }
}
