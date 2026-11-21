package br.com.arenagames.servlet;

import br.com.arenagames.dao.UsuarioDAO;
import br.com.arenagames.model.Usuario;
import br.com.arenagames.util.Web;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Web.usuario(req) != null) {
            Web.redirect(req, resp, "/");
            return;
        }
        Web.render(req, resp, "cadastro");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nome = valor(req, "nome");
        String email = valor(req, "email").toLowerCase();
        String senha = req.getParameter("senha") == null ? "" : req.getParameter("senha");
        String confirmacao = req.getParameter("confirmacao") == null ? "" : req.getParameter("confirmacao");

        List<String> erros = new ArrayList<>();
        if (nome.length() < 3 || nome.length() > 100) {
            erros.add("Informe seu nome (entre 3 e 100 caracteres).");
        }
        if (!EMAIL.matcher(email).matches() || email.length() > 150) {
            erros.add("Informe um e-mail válido.");
        }
        if (senha.length() < 6) {
            erros.add("A senha precisa ter pelo menos 6 caracteres.");
        }
        if (!senha.equals(confirmacao)) {
            erros.add("A confirmação de senha não confere.");
        }

        try {
            if (erros.isEmpty() && usuarioDAO.emailExiste(email)) {
                erros.add("Já existe uma conta com este e-mail.");
            }

            if (!erros.isEmpty()) {
                req.setAttribute("erros", erros);
                req.setAttribute("nome", nome);
                req.setAttribute("email", email);
                Web.render(req, resp, "cadastro");
                return;
            }

            Usuario u = new Usuario();
            u.setNome(nome);
            u.setEmail(email);
            u.setSenhaHash(BCrypt.hashpw(senha, BCrypt.gensalt(10)));
            u.setPerfil("cliente");
            usuarioDAO.inserir(u);

            LoginServlet.iniciarSessao(req, u);
            Web.flash(req, Web.MENSAGEM, "Conta criada com sucesso. Boas compras!");
            Web.redirect(req, resp, "/");
        } catch (SQLIntegrityConstraintViolationException e) {
            req.setAttribute("erros", List.of("Já existe uma conta com este e-mail."));
            req.setAttribute("nome", nome);
            req.setAttribute("email", email);
            Web.render(req, resp, "cadastro");
        } catch (SQLException e) {
            throw new ServletException("Erro ao cadastrar usuario", e);
        }
    }

    private String valor(HttpServletRequest req, String campo) {
        String v = req.getParameter(campo);
        return v == null ? "" : v.trim();
    }
}
