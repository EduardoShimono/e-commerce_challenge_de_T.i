<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="editando" value="${produto.id > 0}"/>
<c:set var="tituloPagina" value="${editando ? 'Admin - Editar produto' : 'Admin - Novo produto'}"/>
<c:set var="menuAdmin" value="produtos"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>
<%@ include file="/WEB-INF/views/partials/admin-menu.jspf" %>

<div class="row justify-content-center">
    <div class="col-lg-9">
        <div class="card card-body p-4">
            <h2 class="h5 mb-3"><c:choose><c:when test="${editando}">Editar produto #${produto.id}</c:when><c:otherwise>Novo produto</c:otherwise></c:choose></h2>

            <c:if test="${not empty erros}">
                <div class="alert alert-danger py-2">
                    <ul class="mb-0 ps-3">
                        <c:forEach var="e" items="${erros}"><li><c:out value="${e}"/></li></c:forEach>
                    </ul>
                </div>
            </c:if>

            <form action="${ctx}/admin/produto-form" method="post" novalidate class="needs-validation">
                <input type="hidden" name="id" value="${produto.id}">

                <div class="row g-3">
                    <div class="col-md-8">
                        <label for="nome" class="form-label">Nome</label>
                        <input type="text" id="nome" name="nome" class="form-control" maxlength="120"
                               value="${fn:escapeXml(produto.nome)}" required>
                        <div class="invalid-feedback">Informe o nome.</div>
                    </div>
                    <div class="col-md-4">
                        <label for="categoriaId" class="form-label">Categoria</label>
                        <select id="categoriaId" name="categoriaId" class="form-select" required>
                            <option value="">Selecione...</option>
                            <c:forEach var="cat" items="${categorias}">
                                <option value="${cat.id}" ${cat.id == produto.categoriaId ? 'selected' : ''}>
                                    <c:out value="${cat.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                        <div class="invalid-feedback">Selecione a categoria.</div>
                    </div>

                    <div class="col-12">
                        <label for="descricao" class="form-label">Descrição</label>
                        <textarea id="descricao" name="descricao" class="form-control" rows="4"><c:out value="${produto.descricao}"/></textarea>
                    </div>

                    <div class="col-md-4">
                        <label for="preco" class="form-label">Preço (R$)</label>
                        <input type="number" id="preco" name="preco" class="form-control" step="0.01" min="0"
                               value="${produto.preco}" required>
                        <div class="invalid-feedback">Informe o preço.</div>
                    </div>
                    <div class="col-md-4">
                        <label for="estoque" class="form-label">Estoque</label>
                        <input type="number" id="estoque" name="estoque" class="form-control" step="1" min="0"
                               value="${produto.estoque}" required>
                        <div class="invalid-feedback">Informe o estoque.</div>
                    </div>
                    <div class="col-md-4 d-flex align-items-end">
                        <div class="form-check form-switch mb-2">
                            <input class="form-check-input" type="checkbox" role="switch" id="ativo" name="ativo"
                                   ${produto.ativo ? 'checked' : ''}>
                            <label class="form-check-label" for="ativo">Ativo na loja</label>
                        </div>
                    </div>

                    <div class="col-12">
                        <label for="imagem" class="form-label">Imagem</label>
                        <input type="text" id="imagem" name="imagem" class="form-control" maxlength="255"
                               value="${fn:escapeXml(produto.imagem)}" placeholder="img/produtos/arquivo.svg ou https://...">
                        <div class="form-text">Caminho de uma imagem dentro de <code>src/main/webapp/img/</code> ou um link externo.</div>
                    </div>
                </div>

                <div class="d-flex justify-content-end gap-2 mt-4">
                    <a href="${ctx}/admin/produtos" class="btn btn-outline-secondary">Cancelar</a>
                    <button type="submit" class="btn btn-arena">Salvar</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
