<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Admin - Pedidos"/>
<c:set var="menuAdmin" value="pedidos"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>
<%@ include file="/WEB-INF/views/partials/admin-menu.jspf" %>

<div class="row g-3 mb-4">
    <div class="col-sm-6 col-lg-3">
        <div class="card card-body">
            <span class="small text-secondary">Pedidos realizados</span>
            <span class="fs-4 fw-bold">${fn:length(pedidos)}</span>
        </div>
    </div>
    <div class="col-sm-6 col-lg-3">
        <div class="card card-body">
            <span class="small text-secondary">Faturamento total</span>
            <span class="fs-4 fw-bold"><fmt:formatNumber value="${faturamento}" type="currency"/></span>
        </div>
    </div>
</div>

<c:if test="${empty pedidos}">
    <div class="alert alert-light border">Nenhum pedido realizado até o momento.</div>
</c:if>

<c:forEach var="p" items="${pedidos}">
    <div class="card mb-3">
        <div class="card-header d-flex flex-wrap justify-content-between gap-2">
            <span><strong>Pedido #${p.id}</strong> &middot; ${p.dataFormatada}</span>
            <span>
                <c:out value="${p.clienteNome}"/>
                <span class="text-secondary">(<c:out value="${p.clienteEmail}"/>)</span>
            </span>
            <span>
                <span class="badge text-bg-light border">${p.formaPagamentoDescricao}</span>
                <span class="badge text-bg-success">${p.status}</span>
            </span>
        </div>
        <div class="table-responsive">
            <table class="table table-sm mb-0">
                <thead>
                <tr>
                    <th class="ps-3">Produto</th>
                    <th class="text-center">Qtd.</th>
                    <th class="text-end">Preço unit.</th>
                    <th class="text-end pe-3">Subtotal</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${p.itens}">
                    <tr>
                        <td class="ps-3"><c:out value="${item.produtoNome}"/></td>
                        <td class="text-center">${item.quantidade}</td>
                        <td class="text-end"><fmt:formatNumber value="${item.precoUnitario}" type="currency"/></td>
                        <td class="text-end pe-3"><fmt:formatNumber value="${item.subtotal}" type="currency"/></td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot>
                <tr class="fw-bold">
                    <td colspan="3" class="text-end">Total do pedido</td>
                    <td class="text-end pe-3"><fmt:formatNumber value="${p.total}" type="currency"/></td>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</c:forEach>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
