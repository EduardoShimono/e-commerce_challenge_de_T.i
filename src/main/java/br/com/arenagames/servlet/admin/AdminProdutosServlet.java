package br.com.arenagames.servlet.admin;

import br.com.arenagames.dao.ProdutoDAO;
import br.com.arenagames.util.Web;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/produtos")
public class AdminProdutosServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("produtos", produtoDAO.listarTodos());
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar produtos", e);
        }
        Web.render(req, resp, "admin/produtos");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        Integer id = Web.inteiro(req.getParameter("id"));

        if (id == null) {
            Web.flash(req, Web.ERRO, "Produto inválido.");
            Web.redirect(req, resp, "/admin/produtos");
            return;
        }

        try {
            switch (acao == null ? "" : acao) {
                case "excluir":
                    if (produtoDAO.excluir(id)) {
                        Web.flash(req, Web.MENSAGEM, "Produto excluído.");
                    } else {
                        Web.flash(req, Web.MENSAGEM,
                                "O produto já possui pedidos, por isso foi apenas desativado.");
                    }
                    break;
                case "desativar":
                    produtoDAO.alterarAtivo(id, false);
                    Web.flash(req, Web.MENSAGEM, "Produto desativado. Ele não aparece mais na loja.");
                    break;
                case "ativar":
                    produtoDAO.alterarAtivo(id, true);
                    Web.flash(req, Web.MENSAGEM, "Produto ativado.");
                    break;
                default:
                    Web.flash(req, Web.ERRO, "Ação inválida.");
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao atualizar produto", e);
        }

        Web.redirect(req, resp, "/admin/produtos");
    }
}
