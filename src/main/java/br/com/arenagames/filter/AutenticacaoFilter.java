package br.com.arenagames.filter;

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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Exige login para finalizar compra e consultar pedidos.
 */
@WebFilter(urlPatterns = {"/checkout", "/pedido", "/meus-pedidos"})
public class AutenticacaoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (Web.usuario(req) == null) {
            Web.flash(req, Web.ERRO, "Entre na sua conta para continuar.");
            String voltar = req.getServletPath();
            Web.redirect(req, resp, "/login?voltar=" + URLEncoder.encode(voltar, StandardCharsets.UTF_8));
            return;
        }
        chain.doFilter(request, response);
    }
}
