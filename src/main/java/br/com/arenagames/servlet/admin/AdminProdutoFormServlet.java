package br.com.arenagames.servlet.admin;

import br.com.arenagames.dao.CategoriaDAO;
import br.com.arenagames.dao.ProdutoDAO;
import br.com.arenagames.model.Produto;
import br.com.arenagames.util.Web;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cadastro e edicao de produtos.
 *   GET  /admin/produto-form          -> formulario vazio
 *   GET  /admin/produto-form?id=10    -> formulario de edicao
 *   POST /admin/produto-form          -> salva (insere ou atualiza)
 */
@WebServlet("/admin/produto-form")
public class AdminProdutoFormServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Web.inteiro(req.getParameter("id"));
        try {
            Produto produto;
            if (id == null) {
                produto = new Produto();
                produto.setAtivo(true);
            } else {
                produto = produtoDAO.buscarPorId(id);
                if (produto == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }
            exibirFormulario(req, resp, produto, null);
        } catch (SQLException e) {
            throw new ServletException("Erro ao abrir o formulario", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> erros = new ArrayList<>();
        Produto p = new Produto();

        Integer id = Web.inteiro(req.getParameter("id"));
        p.setId(id == null ? 0 : id);
        p.setNome(texto(req, "nome"));
        p.setDescricao(texto(req, "descricao"));
        p.setImagem(texto(req, "imagem"));
        p.setAtivo(req.getParameter("ativo") != null);

        Integer categoria = Web.inteiro(req.getParameter("categoriaId"));
        if (categoria == null) {
            erros.add("Selecione uma categoria.");
        } else {
            p.setCategoriaId(categoria);
        }

        if (p.getNome().isEmpty() || p.getNome().length() > 120) {
            erros.add("Informe o nome do produto (até 120 caracteres).");
        }

        try {
            BigDecimal preco = new BigDecimal(texto(req, "preco").replace(",", "."))
                    .setScale(2, RoundingMode.HALF_UP);
            if (preco.compareTo(BigDecimal.ZERO) < 0 || preco.compareTo(new BigDecimal("99999999.99")) > 0) {
                erros.add("Preço fora do intervalo permitido.");
            }
            p.setPreco(preco);
        } catch (NumberFormatException e) {
            erros.add("Informe um preço válido (ex: 199.90).");
        }

        Integer estoque = Web.inteiro(req.getParameter("estoque"));
        if (estoque == null || estoque < 0) {
            erros.add("O estoque deve ser um número inteiro maior ou igual a zero.");
        } else {
            p.setEstoque(estoque);
        }

        String img = p.getImagem();
        if (img.isEmpty()) {
            p.setImagem(null);
        } else if (img.length() > 255
                || !(img.startsWith("img/") || img.startsWith("https://") || img.startsWith("http://"))) {
            erros.add("A imagem deve ser um caminho como img/produtos/arquivo.svg ou um link http(s).");
        }

        try {
            if (!erros.isEmpty()) {
                exibirFormulario(req, resp, p, erros);
                return;
            }

            if (p.getId() > 0) {
                produtoDAO.atualizar(p);
                Web.flash(req, Web.MENSAGEM, "Produto atualizado.");
            } else {
                produtoDAO.inserir(p);
                Web.flash(req, Web.MENSAGEM, "Produto cadastrado.");
            }
            Web.redirect(req, resp, "/admin/produtos");
        } catch (SQLException e) {
            throw new ServletException("Erro ao salvar produto", e);
        }
    }

    private void exibirFormulario(HttpServletRequest req, HttpServletResponse resp, Produto p, List<String> erros)
            throws SQLException, ServletException, IOException {
        req.setAttribute("produto", p);
        req.setAttribute("categorias", categoriaDAO.listar());
        req.setAttribute("erros", erros);
        Web.render(req, resp, "admin/produto-form");
    }

    private String texto(HttpServletRequest req, String campo) {
        String v = req.getParameter(campo);
        return v == null ? "" : v.trim();
    }
}
