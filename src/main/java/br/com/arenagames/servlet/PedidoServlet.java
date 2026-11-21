package br.com.arenagames.servlet;

import br.com.arenagames.dao.PedidoDAO;
import br.com.arenagames.model.Pedido;
import br.com.arenagames.util.Web;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Comprovante de um pedido do cliente logado.
 */
@WebServlet("/pedido")
public class PedidoServlet extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Web.inteiro(req.getParameter("id"));
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Pedido pedido;
        try {
            pedido = pedidoDAO.buscarDoUsuario(id, Web.usuario(req).getId());
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar pedido", e);
        }

        if (pedido == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("pedido", pedido);
        Web.render(req, resp, "pedido");
    }
}
