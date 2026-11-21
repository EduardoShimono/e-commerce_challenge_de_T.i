package br.com.arenagames.util;

import br.com.arenagames.model.Carrinho;
import br.com.arenagames.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Metodos de apoio usados pelos servlets.
 */
public final class Web {

    public static final String USUARIO = "usuarioLogado";
    public static final String CARRINHO = "carrinho";
    public static final String MENSAGEM = "mensagem";
    public static final String ERRO = "erro";

    private Web() {
    }

    public static void render(HttpServletRequest req, HttpServletResponse resp, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, resp);
    }

    public static void redirect(HttpServletRequest req, HttpServletResponse resp, String caminho)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + caminho);
    }

    public static Usuario usuario(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (Usuario) s.getAttribute(USUARIO);
    }

    public static Carrinho carrinho(HttpServletRequest req) {
        HttpSession s = req.getSession();
        Carrinho c = (Carrinho) s.getAttribute(CARRINHO);
        if (c == null) {
            c = new Carrinho();
            s.setAttribute(CARRINHO, c);
        }
        return c;
    }

    /** Mensagem exibida uma unica vez na proxima pagina. */
    public static void flash(HttpServletRequest req, String tipo, String texto) {
        req.getSession().setAttribute(tipo, texto);
    }

    public static Integer inteiro(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static int inteiro(String valor, int padrao) {
        Integer v = inteiro(valor);
        return v == null ? padrao : v;
    }
}
