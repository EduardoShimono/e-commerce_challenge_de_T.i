<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Pedido #${pedido.id}"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card card-body p-4">
            <div class="text-center mb-4">
                <i class="bi bi-check-circle-fill text-success display-5"></i>
                <h1 class="h4 mt-2">Pedido #${pedido.id} confirmado</h1>
                <p class="text-secondary mb-0">Realizado em ${pedido.dataFormatada}
                    &middot; Pagamento: ${pedido.formaPagamentoDescricao}
                    &middot; Status: <span class="badge text-bg-success">${pedido.status}</span></p>
            </div>

            <table class="table">
                <thead class="table-light">
                <tr>
                    <th>Produto</th>
                    <th class="text-center">Qtd.</th>
                    <th class="text-end">Preço unit.</th>
                    <th class="text-end">Subtotal</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${pedido.itens}">
                    <tr>
                        <td><c:out value="${item.produtoNome}"/></td>
                        <td class="text-center">${item.quantidade}</td>
                        <td class="text-end"><fmt:formatNumber value="${item.precoUnitario}" type="currency"/></td>
                        <td class="text-end"><fmt:formatNumber value="${item.subtotal}" type="currency"/></td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot>
                <tr class="fw-bold">
                    <td colspan="3" class="text-end">Total</td>
                    <td class="text-end"><fmt:formatNumber value="${pedido.total}" type="currency"/></td>
                </tr>
                </tfoot>
            </table>

            <div class="d-flex justify-content-between">
                <a href="${ctx}/meus-pedidos" class="btn btn-outline-secondary">Meus pedidos</a>
                <a href="${ctx}/" class="btn btn-arena">Voltar para a loja</a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
