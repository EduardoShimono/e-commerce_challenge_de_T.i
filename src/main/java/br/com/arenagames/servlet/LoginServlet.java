package br.com.arenagames.servlet;

import br.com.arenagames.dao.UsuarioDAO;
import br.com.arenagames.model.Carrinho;
import br.com.arenagames.model.Usuario;
import br.com.arenagames.util.Web;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Web.usuario(req) != null) {
            Web.redirect(req, resp, "/");
            return;
        }
        req.setAttribute("voltar", destinoSeguro(req.getParameter("voltar")));
        Web.render(req, resp, "login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email") == null ? "" : req.getParameter("email").trim().toLowerCase();
        String senha = req.getParameter("senha") == null ? "" : req.getParameter("senha");
        String voltar = destinoSeguro(req.getParameter("voltar"));

        Usuario usuario;
        try {
            usuario = usuarioDAO.buscarPorEmail(email);
        } catch (SQLException e) {
            throw new ServletException("Erro ao autenticar", e);
        }

        if (usuario == null || !senhaConfere(senha, usuario.getSenhaHash())) {
            req.setAttribute("erroLogin", "E-mail ou senha incorretos.");
            req.setAttribute("email", email);
            req.setAttribute("voltar", voltar);
            Web.render(req, resp, "login");
            return;
        }

        iniciarSessao(req, usuario);
        Web.flash(req, Web.MENSAGEM, "Bem-vindo(a), " + usuario.getNome() + "!");

        if (voltar.isEmpty()) {
            voltar = usuario.isAdmin() ? "/admin/produtos" : "/";
        }
        Web.redirect(req, resp, voltar);
    }

    /**
     * Gera uma nova sessao apos o login (evita fixacao de sessao),
     * mantendo o carrinho que o visitante ja tinha montado.
     */
    static void iniciarSessao(HttpServletRequest req, Usuario usuario) {
        HttpSession antiga = req.getSession(false);
        Carrinho carrinho = null;
        if (antiga != null) {
            carrinho = (Carrinho) antiga.getAttribute(Web.CARRINHO);
            antiga.invalidate();
        }

        HttpSession nova = req.getSession(true);
        nova.setMaxInactiveInterval(60 * 60);
        usuario.setSenhaHash(null);
        nova.setAttribute(Web.USUARIO, usuario);
        if (carrinho != null) {
            nova.setAttribute(Web.CARRINHO, carrinho);
        }
    }

    private boolean senhaConfere(String senha, String hash) {
        try {
            return BCrypt.checkpw(senha, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /** Aceita apenas caminhos internos, para nao redirecionar para outro site. */
    private String destinoSeguro(String voltar) {
        if (voltar == null || !voltar.startsWith("/") || voltar.startsWith("//") || voltar.contains("\\")) {
            return "";
        }
        return voltar;
    }
}
