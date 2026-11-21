<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Erro no servidor"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<div class="text-center py-5">
    <i class="bi bi-exclamation-octagon display-3 text-secondary"></i>
    <h1 class="h3 mt-3">Erro no servidor</h1>
    <p class="text-secondary">Algo deu errado ao processar sua solicitação. Verifique se o banco de dados está ligado e tente novamente.</p>
    <a href="${ctx}/" class="btn btn-arena">Voltar para a loja</a>
</div>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
