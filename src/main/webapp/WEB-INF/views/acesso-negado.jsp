<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Acesso negado"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<div class="text-center py-5">
    <i class="bi bi-shield-lock display-3 text-secondary"></i>
    <h1 class="h3 mt-3">Acesso negado</h1>
    <p class="text-secondary">Esta área é restrita a administradores.</p>
    <a href="${ctx}/" class="btn btn-arena">Voltar para a loja</a>
</div>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
