<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Meus pedidos"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<h1 class="h3 mb-4">Meus pedidos</h1>

<c:if test="${empty pedidos}">
    <div class="alert alert-light border">Você ainda não fez nenhum pedido. <a href="${ctx}/">Ir para a loja</a></div>
</c:if>

<div class="accordion" id="listaPedidos">
    <c:forEach var="p" items="${pedidos}">
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                        data-bs-target="#pedido-${p.id}" aria-expanded="false">
                    <span class="me-auto">Pedido #${p.id} &middot; ${p.dataFormatada}</span>
                    <span class="me-3 fw-semibold"><fmt:formatNumber value="${p.total}" type="currency"/></span>
                </button>
            </h2>
            <div id="pedido-${p.id}" class="accordion-collapse collapse" data-bs-parent="#listaPedidos">
                <div class="accordion-body">
                    <p class="small text-secondary">Pagamento: ${p.formaPagamentoDescricao} &middot; Status: ${p.status}</p>
                    <ul class="list-group list-group-flush">
                        <c:forEach var="item" items="${p.itens}">
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span>${item.quantidade}x <c:out value="${item.produtoNome}"/></span>
                                <span><fmt:formatNumber value="${item.subtotal}" type="currency"/></span>
                            </li>
                        </c:forEach>
                    </ul>
                    <a href="${ctx}/pedido?id=${p.id}" class="btn btn-sm btn-outline-secondary mt-2">Ver comprovante</a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
