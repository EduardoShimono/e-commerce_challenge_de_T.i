<%@ include file="/WEB-INF/views/partials/taglibs.jspf" %>
<c:set var="tituloPagina" value="Criar conta"/>
<%@ include file="/WEB-INF/views/partials/topo.jspf" %>

<div class="row justify-content-center">
    <div class="col-sm-10 col-md-8 col-lg-6">
        <div class="card card-body p-4">
            <h1 class="h4 mb-3">Criar conta</h1>

            <c:if test="${not empty erros}">
                <div class="alert alert-danger py-2">
                    <ul class="mb-0 ps-3">
                        <c:forEach var="e" items="${erros}"><li><c:out value="${e}"/></li></c:forEach>
                    </ul>
                </div>
            </c:if>

            <form action="${ctx}/cadastro" method="post" novalidate class="needs-validation">
                <div class="mb-3">
                    <label for="nome" class="form-label">Nome completo</label>
                    <input type="text" id="nome" name="nome" class="form-control" minlength="3" maxlength="100"
                           value="${fn:escapeXml(nome)}" required>
                    <div class="invalid-feedback">Informe seu nome (mínimo 3 caracteres).</div>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">E-mail</label>
                    <input type="email" id="email" name="email" class="form-control" maxlength="150"
                           value="${fn:escapeXml(email)}" required>
                    <div class="invalid-feedback">Informe um e-mail válido.</div>
                </div>
                <div class="row">
                    <div class="col-sm-6 mb-3">
                        <label for="senha" class="form-label">Senha</label>
                        <input type="password" id="senha" name="senha" class="form-control" minlength="6" required>
                        <div class="invalid-feedback">Mínimo de 6 caracteres.</div>
                    </div>
                    <div class="col-sm-6 mb-3">
                        <label for="confirmacao" class="form-label">Confirmar senha</label>
                        <input type="password" id="confirmacao" name="confirmacao" class="form-control"
                               minlength="6" data-igual="senha" required>
                        <div class="invalid-feedback">As senhas precisam ser iguais.</div>
                    </div>
                </div>
                <button type="submit" class="btn btn-arena w-100">Cadastrar</button>
            </form>

            <p class="text-center small mt-3 mb-0">
                Já tem conta? <a href="${ctx}/login">Entrar</a>
            </p>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/partials/rodape.jspf" %>
