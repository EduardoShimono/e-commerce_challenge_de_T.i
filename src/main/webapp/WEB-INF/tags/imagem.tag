<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="produto" required="true" type="br.com.arenagames.model.Produto" %>
<%@ attribute name="classe" required="false" %>
<img src="${produto.imagemExterna ? '' : pageContext.request.contextPath.concat('/')}${fn:escapeXml(produto.imagemOuPadrao)}"
     alt="${fn:escapeXml(produto.nome)}" class="${classe}" loading="lazy">
