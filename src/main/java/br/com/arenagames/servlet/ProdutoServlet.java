package br.com.arenagames.servlet;

import br.com.arenagames.dao.ProdutoDAO;
import br.com.arenagames.model.Produto;
import br.com.arenagames.util.Web;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/produto")
public class ProdutoServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Web.inteiro(req.getParameter("id"));
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Produto produto;
        try {
            produto = produtoDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar produto", e);
        }

        if (produto == null || !produto.isAtivo()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        int noCarrinho = Web.carrinho(req).getQuantidade(produto.getId());
        req.setAttribute("produto", produto);
        req.setAttribute("noCarrinho", noCarrinho);
        req.setAttribute("maxAdicionar", Math.max(0, produto.getEstoque() - noCarrinho));
        Web.render(req, resp, "produto");
    }
}
