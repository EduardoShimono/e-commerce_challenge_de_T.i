<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Entrar"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<div class="row justify-content-center">
    <div class="col-sm-10 col-md-7 col-lg-5">
        <div class="card card-body p-4">
            <h1 class="h4 mb-3">Entrar</h1>

            <c:if test="${not empty erroLogin}">
                <div class="alert alert-danger py-2"><c:out value="${erroLogin}"/></div>
            </c:if>

            <form action="${ctx}/login" method="post" novalidate class="needs-validation">
                <input type="hidden" name="voltar" value="${fn:escapeXml(voltar)}">

                <div class="mb-3">
                    <label for="email" class="form-label">E-mail</label>
                    <input type="email" id="email" name="email" class="form-control"
                           value="${fn:escapeXml(email)}" required autofocus>
                    <div class="invalid-feedback">Informe um e-mail válido.</div>
                </div>
                <div class="mb-3">
                    <label for="senha" class="form-label">Senha</label>
                    <input type="password" id="senha" name="senha" class="form-control" required>
                    <div class="invalid-feedback">Informe sua senha.</div>
                </div>
                <button type="submit" class="btn btn-arena w-100">Entrar</button>
            </form>

            <p class="text-center small mt-3 mb-0">
                Ainda não tem conta? <a href="${ctx}/cadastro">Cadastre-se</a>
            </p>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
