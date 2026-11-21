<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Finalizar compra"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<h1 class="h3 mb-4">Finalizar compra</h1>

<c:forEach var="aviso" items="${avisos}">
    <div class="alert alert-warning"><c:out value="${aviso}"/></div>
</c:forEach>

<form action="${ctx}/checkout" method="post" id="form-checkout">
<div class="row g-4">
    <div class="col-lg-7">
        <div class="card card-body mb-3">
            <h2 class="h5">Cliente</h2>
            <p class="mb-0">
                <c:out value="${sessionScope.usuarioLogado.nome}"/><br>
                <span class="text-secondary"><c:out value="${sessionScope.usuarioLogado.email}"/></span>
            </p>
        </div>

        <div class="card card-body">
            <h2 class="h5 mb-3">Forma de pagamento</h2>

            <div class="opcoes-pagamento">
                <label class="opcao-pagamento">
                    <input class="form-check-input" type="radio" name="formaPagamento" value="PIX" required checked>
                    <span><i class="bi bi-qr-code"></i> <strong>PIX</strong><br>
                        <small class="text-secondary">Aprovação imediata</small></span>
                </label>
                <label class="opcao-pagamento">
                    <input class="form-check-input" type="radio" name="formaPagamento" value="BOLETO">
                    <span><i class="bi bi-upc"></i> <strong>Boleto</strong><br>
                        <small class="text-secondary">Compensação simulada</small></span>
                </label>
                <label class="opcao-pagamento">
                    <input class="form-check-input" type="radio" name="formaPagamento" value="CARTAO">
                    <span><i class="bi bi-credit-card"></i> <strong>Cartão de crédito</strong><br>
                        <small class="text-secondary">À vista</small></span>
                </label>
            </div>

            <div id="detalhe-PIX" class="detalhe-pagamento mt-3">
                <div class="d-flex gap-3 align-items-center">
                    <div class="qr-falso" aria-hidden="true"></div>
                    <p class="small mb-0">Ao confirmar, o pagamento via PIX será aprovado automaticamente
                        (ambiente de simulação).</p>
                </div>
            </div>
            <div id="detalhe-BOLETO" class="detalhe-pagamento mt-3 d-none">
                <p class="small mb-0">Linha digitável (simulada):<br>
                    <code>34191.79001 01043.510047 91020.150008 1 00000000000000</code></p>
            </div>
            <div id="detalhe-CARTAO" class="detalhe-pagamento mt-3 d-none">
                <p class="small text-secondary">Os dados do cartão não são enviados nem armazenados. Preenchimento apenas ilustrativo.</p>
                <div class="row g-2">
                    <div class="col-12">
                        <input type="text" class="form-control" placeholder="Número do cartão" inputmode="numeric" maxlength="19" data-mascara="cartao">
                    </div>
                    <div class="col-6">
                        <input type="text" class="form-control" placeholder="MM/AA" maxlength="5" data-mascara="validade">
                    </div>
                    <div class="col-6">
                        <input type="text" class="form-control" placeholder="CVV" inputmode="numeric" maxlength="4">
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-lg-5">
        <div class="card card-body resumo">
            <h2 class="h5">Resumo do pedido</h2>
            <c:forEach var="item" items="${itens}">
                <div class="d-flex justify-content-between small py-1">
                    <span>${item.quantidade}x <c:out value="${item.produto.nome}"/></span>
                    <span class="text-nowrap ms-2"><fmt:formatNumber value="${item.subtotal}" type="currency"/></span>
                </div>
            </c:forEach>
            <hr>
            <div class="d-flex justify-content-between fs-5 fw-bold">
                <span>Total</span>
                <span><fmt:formatNumber value="${total}" type="currency"/></span>
            </div>
            <button type="submit" class="btn btn-arena btn-lg mt-3">Confirmar pagamento</button>
            <a href="${ctx}/carrinho" class="btn btn-link btn-sm mt-1">Voltar ao carrinho</a>
        </div>
    </div>
</div>
</form>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
