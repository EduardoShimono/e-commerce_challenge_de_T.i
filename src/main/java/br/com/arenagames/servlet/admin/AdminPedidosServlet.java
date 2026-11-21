package br.com.arenagames.servlet.admin;

import br.com.arenagames.dao.PedidoDAO;
import br.com.arenagames.model.Pedido;
import br.com.arenagames.util.Web;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/pedidos")
public class AdminPedidosServlet extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Pedido> pedidos;
        try {
            pedidos = pedidoDAO.listarTodos();
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar pedidos", e);
        }

        BigDecimal faturamento = BigDecimal.ZERO;
        for (Pedido p : pedidos) {
            faturamento = faturamento.add(p.getTotal());
        }

        req.setAttribute("pedidos", pedidos);
        req.setAttribute("faturamento", faturamento);
        Web.render(req, resp, "admin/pedidos");
    }
}
