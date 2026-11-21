<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Carrinho"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<h1 class="h3 mb-4"><i class="bi bi-cart3"></i> Meu carrinho</h1>

<c:forEach var="aviso" items="${avisos}">
    <div class="alert alert-warning"><c:out value="${aviso}"/></div>
</c:forEach>

<c:choose>
    <c:when test="${empty itens}">
        <div class="text-center py-5">
            <i class="bi bi-cart-x display-4 text-secondary"></i>
            <p class="mt-3">Seu carrinho está vazio.</p>
            <a href="${ctx}/" class="btn btn-arena">Ver produtos</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="row g-4">
            <div class="col-lg-8">
                <div class="card">
                    <div class="table-responsive">
                        <table class="table align-middle mb-0 tabela-carrinho">
                            <thead class="table-light">
                            <tr>
                                <th>Produto</th>
                                <th class="text-end">Preço</th>
                                <th class="text-center" style="width: 170px;">Quantidade</th>
                                <th class="text-end">Subtotal</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${itens}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-2">
                                            <t:imagem produto="${item.produto}" classe="miniatura"/>
                                            <div>
                                                <a href="${ctx}/produto?id=${item.produto.id}" class="text-reset">
                                                    <c:out value="${item.produto.nome}"/>
                                                </a>
                                                <div class="small text-secondary">Estoque: ${item.produto.estoque}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="text-end text-nowrap"><fmt:formatNumber value="${item.produto.preco}" type="currency"/></td>
                                    <td>
                                        <form action="${ctx}/carrinho" method="post" class="d-flex gap-1 form-quantidade">
                                            <input type="hidden" name="acao" value="alterar">
                                            <input type="hidden" name="produtoId" value="${item.produto.id}">
                                            <input type="number" name="quantidade" value="${item.quantidade}"
                                                   min="1" max="${item.produto.estoque}" class="form-control form-control-sm" required>
                                            <button type="submit" class="btn btn-sm btn-outline-secondary" title="Atualizar quantidade">
                                                <i class="bi bi-arrow-repeat"></i>
                                            </button>
                                        </form>
                                    </td>
                                    <td class="text-end text-nowrap fw-semibold"><fmt:formatNumber value="${item.subtotal}" type="currency"/></td>
                                    <td class="text-end">
                                        <form action="${ctx}/carrinho" method="post">
                                            <input type="hidden" name="acao" value="remover">
                                            <input type="hidden" name="produtoId" value="${item.produto.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger" title="Remover">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="d-flex justify-content-between mt-3">
                    <a href="${ctx}/" class="btn btn-link px-0"><i class="bi bi-arrow-left"></i> Continuar comprando</a>
                    <form action="${ctx}/carrinho" method="post" data-confirmar="Deseja esvaziar o carrinho?">
                        <input type="hidden" name="acao" value="limpar">
                        <button type="submit" class="btn btn-sm btn-outline-secondary">Esvaziar carrinho</button>
                    </form>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="card card-body resumo">
                    <h2 class="h5">Resumo</h2>
                    <c:forEach var="item" items="${itens}">
                        <div class="d-flex justify-content-between small">
                            <span>${item.quantidade}x <c:out value="${item.produto.nome}"/></span>
                            <span class="text-nowrap ms-2"><fmt:formatNumber value="${item.subtotal}" type="currency"/></span>
                        </div>
                    </c:forEach>
                    <hr>
                    <div class="d-flex justify-content-between fs-5 fw-bold">
                        <span>Total</span>
                        <span><fmt:formatNumber value="${total}" type="currency"/></span>
                    </div>
                    <a href="${ctx}/checkout" class="btn btn-arena btn-lg mt-3">Finalizar compra</a>
                    <c:if test="${empty sessionScope.usuarioLogado}">
                        <p class="small text-secondary mt-2 mb-0">Você precisará entrar ou criar uma conta para finalizar.</p>
                    </c:if>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
