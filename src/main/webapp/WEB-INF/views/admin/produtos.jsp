<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Admin - Produtos"/>
<c:set var="menuAdmin" value="produtos"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>
<%@ include file="/WEB-INF/views/partials/admin-menu.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="h5 mb-0">Produtos cadastrados (${fn:length(produtos)})</h2>
    <a href="${ctx}/admin/produto-form" class="btn btn-arena"><i class="bi bi-plus-lg"></i> Novo produto</a>
</div>

<div class="card">
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
            <tr>
                <th>#</th>
                <th>Produto</th>
                <th>Categoria</th>
                <th class="text-end">Preço</th>
                <th class="text-center">Estoque</th>
                <th class="text-center">Situação</th>
                <th class="text-end">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="p" items="${produtos}">
                <tr class="${p.ativo ? '' : 'table-secondary text-secondary'}">
                    <td>${p.id}</td>
                    <td>
                        <div class="d-flex align-items-center gap-2">
                            <t:imagem produto="${p}" classe="miniatura"/>
                            <c:out value="${p.nome}"/>
                        </div>
                    </td>
                    <td><c:out value="${p.categoriaNome}"/></td>
                    <td class="text-end text-nowrap"><fmt:formatNumber value="${p.preco}" type="currency"/></td>
                    <td class="text-center">
                        <span class="${p.estoque == 0 ? 'text-danger fw-bold' : ''}">${p.estoque}</span>
                    </td>
                    <td class="text-center">
                        <c:choose>
                            <c:when test="${p.ativo}"><span class="badge text-bg-success">Ativo</span></c:when>
                            <c:otherwise><span class="badge text-bg-secondary">Inativo</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-end text-nowrap">
                        <a href="${ctx}/admin/produto-form?id=${p.id}" class="btn btn-sm btn-outline-primary" title="Editar">
                            <i class="bi bi-pencil"></i>
                        </a>
                        <form action="${ctx}/admin/produtos" method="post" class="d-inline">
                            <input type="hidden" name="id" value="${p.id}">
                            <c:choose>
                                <c:when test="${p.ativo}">
                                    <input type="hidden" name="acao" value="desativar">
                                    <button type="submit" class="btn btn-sm btn-outline-secondary" title="Desativar">
                                        <i class="bi bi-eye-slash"></i>
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="acao" value="ativar">
                                    <button type="submit" class="btn btn-sm btn-outline-success" title="Ativar">
                                        <i class="bi bi-eye"></i>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                        <form action="${ctx}/admin/produtos" method="post" class="d-inline"
                              data-confirmar="Excluir o produto &quot;${fn:escapeXml(p.nome)}&quot;?">
                            <input type="hidden" name="id" value="${p.id}">
                            <input type="hidden" name="acao" value="excluir">
                            <button type="submit" class="btn btn-sm btn-outline-danger" title="Excluir">
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

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
