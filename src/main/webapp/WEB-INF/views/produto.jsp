<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="${produto.nome}"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="${ctx}/">Loja</a></li>
        <li class="breadcrumb-item"><a href="${ctx}/loja?categoria=${produto.categoriaId}"><c:out value="${produto.categoriaNome}"/></a></li>
        <li class="breadcrumb-item active" aria-current="page"><c:out value="${produto.nome}"/></li>
    </ol>
</nav>

<div class="row g-4">
    <div class="col-md-6">
        <div class="card produto-detalhe-img">
            <t:imagem produto="${produto}" classe="img-fluid rounded"/>
        </div>
    </div>

    <div class="col-md-6">
        <p class="text-secondary small mb-1">Código: #${produto.id}</p>
        <h1 class="h3"><c:out value="${produto.nome}"/></h1>
        <p class="preco preco-grande my-3"><fmt:formatNumber value="${produto.preco}" type="currency"/></p>

        <p class="mb-3">
            <c:choose>
                <c:when test="${produto.estoque == 0}">
                    <span class="badge text-bg-secondary fs-6">Esgotado</span>
                </c:when>
                <c:when test="${produto.estoque <= 5}">
                    <span class="badge text-bg-warning fs-6">Apenas ${produto.estoque} em estoque</span>
                </c:when>
                <c:otherwise>
                    <span class="badge text-bg-success fs-6">Disponível: ${produto.estoque} unidades</span>
                </c:otherwise>
            </c:choose>
        </p>

        <h2 class="h6 text-uppercase text-secondary">Descrição</h2>
        <p><c:out value="${produto.descricao}"/></p>

        <c:choose>
            <c:when test="${maxAdicionar > 0}">
                <form action="${ctx}/carrinho" method="post" class="row g-2 align-items-end mt-3">
                    <input type="hidden" name="acao" value="adicionar">
                    <input type="hidden" name="produtoId" value="${produto.id}">
                    <div class="col-4 col-sm-3">
                        <label for="quantidade" class="form-label small">Quantidade</label>
                        <input type="number" id="quantidade" name="quantidade" class="form-control"
                               value="1" min="1" max="${maxAdicionar}" required>
                    </div>
                    <div class="col">
                        <button type="submit" class="btn btn-arena btn-lg w-100">
                            <i class="bi bi-cart-plus"></i> Adicionar ao carrinho
                        </button>
                    </div>
                </form>
                <c:if test="${noCarrinho > 0}">
                    <p class="small text-secondary mt-2">Você já tem ${noCarrinho} unidade(s) no carrinho.</p>
                </c:if>
            </c:when>
            <c:when test="${noCarrinho > 0}">
                <div class="alert alert-info mt-3">Todo o estoque disponível deste produto já está no seu carrinho.</div>
            </c:when>
            <c:otherwise>
                <button class="btn btn-secondary btn-lg w-100 mt-3" disabled>Produto indisponível</button>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
