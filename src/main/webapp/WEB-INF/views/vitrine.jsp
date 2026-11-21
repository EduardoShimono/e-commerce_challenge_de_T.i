<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Loja"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<c:if test="${empty busca and empty categoriaSelecionada}">
    <section class="destaque mb-4">
        <div>
            <h1 class="display-6 fw-bold mb-2">Seu próximo jogo está aqui.</h1>
            <p class="mb-0">Consoles, jogos e periféricos com entrega rápida e pagamento por PIX, boleto ou cartão.</p>
        </div>
    </section>
</c:if>

<div class="row g-4">
    <aside class="col-lg-3">
        <form action="${ctx}/loja" method="get" class="card card-body filtros">
            <h2 class="h6 text-uppercase text-secondary mb-3">Filtrar produtos</h2>

            <label for="busca" class="form-label small">Buscar</label>
            <input type="search" id="busca" name="busca" class="form-control mb-3"
                   value="${fn:escapeXml(busca)}" placeholder="Nome do produto">

            <label for="categoria" class="form-label small">Categoria</label>
            <select id="categoria" name="categoria" class="form-select mb-3">
                <option value="">Todas</option>
                <c:forEach var="cat" items="${categorias}">
                    <option value="${cat.id}" ${cat.id == categoriaSelecionada ? 'selected' : ''}>
                        <c:out value="${cat.nome}"/>
                    </option>
                </c:forEach>
            </select>

            <button type="submit" class="btn btn-arena w-100">Aplicar</button>
            <c:if test="${not empty busca or not empty categoriaSelecionada}">
                <a href="${ctx}/" class="btn btn-link btn-sm mt-1">Limpar filtros</a>
            </c:if>
        </form>

        <div class="list-group mt-3 d-none d-lg-block">
            <a href="${ctx}/" class="list-group-item list-group-item-action ${empty categoriaSelecionada ? 'active' : ''}">Todas as categorias</a>
            <c:forEach var="cat" items="${categorias}">
                <a href="${ctx}/loja?categoria=${cat.id}"
                   class="list-group-item list-group-item-action ${cat.id == categoriaSelecionada ? 'active' : ''}">
                    <c:out value="${cat.nome}"/>
                </a>
            </c:forEach>
        </div>
    </aside>

    <section class="col-lg-9">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="h5 mb-0">
                <c:choose>
                    <c:when test="${not empty busca}">Resultados para "<c:out value="${busca}"/>"</c:when>
                    <c:otherwise>Produtos</c:otherwise>
                </c:choose>
            </h2>
            <span class="text-secondary small">${fn:length(produtos)} item(ns)</span>
        </div>

        <c:if test="${empty produtos}">
            <div class="alert alert-light border">Nenhum produto encontrado com esses filtros.</div>
        </c:if>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-xl-3 g-3">
            <c:forEach var="p" items="${produtos}">
                <div class="col">
                    <div class="card h-100 produto-card">
                        <a href="${ctx}/produto?id=${p.id}" class="produto-img">
                            <t:imagem produto="${p}" classe="card-img-top"/>
                        </a>
                        <div class="card-body d-flex flex-column">
                            <span class="badge text-bg-light align-self-start mb-2"><c:out value="${p.categoriaNome}"/></span>
                            <h3 class="h6 card-title">
                                <a href="${ctx}/produto?id=${p.id}" class="stretched-link text-reset text-decoration-none">
                                    <c:out value="${p.nome}"/>
                                </a>
                            </h3>
                            <div class="mt-auto d-flex justify-content-between align-items-end">
                                <span class="preco"><fmt:formatNumber value="${p.preco}" type="currency"/></span>
                                <c:choose>
                                    <c:when test="${p.estoque == 0}"><span class="badge text-bg-secondary">Esgotado</span></c:when>
                                    <c:when test="${p.estoque <= 5}"><span class="badge text-bg-warning">Últimas ${p.estoque}</span></c:when>
                                    <c:otherwise><span class="badge text-bg-success">Em estoque</span></c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>
</div>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
