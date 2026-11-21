package br.com.arenagames.servlet;

import br.com.arenagames.dao.PedidoDAO;
import br.com.arenagames.model.Carrinho;
import br.com.arenagames.model.ItemCarrinho;
import br.com.arenagames.model.Usuario;
import br.com.arenagames.util.CarrinhoHelper;
import br.com.arenagames.util.EstoqueInsuficienteException;
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
import java.util.Set;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final Set<String> FORMAS_PAGAMENTO = Set.of("PIX", "BOLETO", "CARTAO");

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Carrinho carrinho = Web.carrinho(req);
        List<ItemCarrinho> itens = new ArrayList<>();
        try {
            req.setAttribute("avisos", CarrinhoHelper.sincronizar(carrinho, itens));
        } catch (SQLException e) {
            throw new ServletException("Erro ao carregar o checkout", e);
        }

        if (itens.isEmpty()) {
            Web.flash(req, Web.ERRO, "Seu carrinho está vazio.");
            Web.redirect(req, resp, "/carrinho");
            return;
        }

        req.setAttribute("itens", itens);
        req.setAttribute("total", CarrinhoHelper.total(itens));
        Web.render(req, resp, "checkout");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuario = Web.usuario(req);
        Carrinho carrinho = Web.carrinho(req);
        String forma = req.getParameter("formaPagamento");

        if (carrinho.isVazio()) {
            Web.flash(req, Web.ERRO, "Seu carrinho está vazio.");
            Web.redirect(req, resp, "/carrinho");
            return;
        }
        if (forma == null || !FORMAS_PAGAMENTO.contains(forma)) {
            Web.flash(req, Web.ERRO, "Selecione uma forma de pagamento.");
            Web.redirect(req, resp, "/checkout");
            return;
        }

        try {
            int pedidoId = pedidoDAO.finalizar(usuario.getId(), forma, carrinho);
            carrinho.limpar();
            Web.flash(req, Web.MENSAGEM, "Pagamento aprovado! Pedido #" + pedidoId + " realizado com sucesso.");
            Web.redirect(req, resp, "/pedido?id=" + pedidoId);
        } catch (EstoqueInsuficienteException e) {
            Web.flash(req, Web.ERRO, e.getMessage());
            Web.redirect(req, resp, "/carrinho");
        } catch (SQLException e) {
            throw new ServletException("Erro ao finalizar o pedido", e);
        }
    }
}
