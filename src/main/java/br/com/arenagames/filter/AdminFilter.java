package br.com.arenagames.filter;

import br.com.arenagames.model.Usuario;
import br.com.arenagames.util.Web;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Protege todas as rotas do painel administrativo.
 * A verificacao e feita no servidor, a partir do usuario guardado na sessao.
 */
@WebFilter(urlPatterns = {"/admin", "/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Usuario usuario = Web.usuario(req);
        if (usuario == null) {
            Web.flash(req, Web.ERRO, "Entre com uma conta de administrador.");
            Web.redirect(req, resp, "/login?voltar=/admin/produtos");
            return;
        }
        if (!usuario.isAdmin()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Web.render(req, resp, "acesso-negado");
            return;
        }
        chain.doFilter(request, response);
    }
}
