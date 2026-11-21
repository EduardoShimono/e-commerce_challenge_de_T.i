package br.com.arenagames.servlet;

import br.com.arenagames.dao.CategoriaDAO;
import br.com.arenagames.dao.ProdutoDAO;
import br.com.arenagames.util.Web;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"", "/loja"})
public class VitrineServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String busca = req.getParameter("busca");
        Integer categoria = Web.inteiro(req.getParameter("categoria"));

        try {
            req.setAttribute("produtos", produtoDAO.listarVitrine(busca, categoria));
            req.setAttribute("categorias", categoriaDAO.listar());
        } catch (SQLException e) {
            throw new ServletException("Erro ao carregar a vitrine", e);
        }

        req.setAttribute("busca", busca == null ? "" : busca);
        req.setAttribute("categoriaSelecionada", categoria);
        Web.render(req, resp, "vitrine");
    }
}
