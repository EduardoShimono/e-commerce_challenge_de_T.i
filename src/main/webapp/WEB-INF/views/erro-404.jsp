<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Página não encontrada"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<div class="text-center py-5">
    <i class="bi bi-signpost-split display-3 text-secondary"></i>
    <h1 class="h3 mt-3">Página não encontrada</h1>
    <p class="text-secondary">O endereço que você tentou acessar não existe ou o produto não está mais disponível.</p>
    <a href="${ctx}/" class="btn btn-arena">Voltar para a loja</a>
</div>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
