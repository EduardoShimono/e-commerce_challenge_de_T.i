package br.com.arenagames.servlet;

import br.com.arenagames.dao.PedidoDAO;
import br.com.arenagames.util.Web;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/meus-pedidos")
public class MeusPedidosServlet extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("pedidos", pedidoDAO.listarPorUsuario(Web.usuario(req).getId()));
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar pedidos", e);
        }
        Web.render(req, resp, "meus-pedidos");
    }
}
