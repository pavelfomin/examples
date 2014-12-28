<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>

<head>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

</head>
<body>

<h1>Solr Search</h1>

<form:form modelAttribute="searchCommand" action="search.do">
	<input id="page" name="page" type="hidden" value="0">
	<input id="size" name="size" type="hidden" value="15">
	<label for="searchTerm"></label>
	<form:input path="searchTerm" />
	<button class="action-page-submit" data-page="0" data-size="15">Search</button>
</form:form>

<c:set var="next" value="${page.nextPageable()}" />
<c:set var="prev" value="${page.previousPageable()}" />

<div class="pagination">
	<c:if test="${not empty page}">
		<h5>${page.totalElements} Matches Found</h5>
	</c:if>

	<c:if test="${page.totalPages > 1}">
		<span>Showing ${page.numberOfElements} matches.</span>
		<span>Page ${page.number + 1} of ${page.totalPages}.</span>
	</c:if>

	<c:if test="${not empty prev}">
		<button class="action-page-submit" data-page="0" data-size="${prev.pageSize}">First</button>
		<button class="action-page-submit" data-page="${prev.pageNumber}" data-size="${prev.pageSize}"><i class="fa fa-caret-left"></i> Prev</button>
	</c:if>
	<c:if test="${not empty next}">
		<button class="action-page-submit" data-page="${next.pageNumber}" data-size="${next.pageSize}">Next <i class="fa fa-caret-right"></i></button>
		<button class="action-page-submit" data-page="${page.totalPages - 1}" data-size="${next.pageSize}">Last</button>
	</c:if>
</div>

<c:forEach var="entry" items="${page.highlighted}">
	<c:set var="document" value="${entry.entity}" />
	<dl>
		<dt><a href="${document.url}">${document.highlightedTitle}</a></dt>
		<dd>${document.highlightedUrl}</dd>
		<dd>${document.highlightedContent}</dd>
	</dl>
</c:forEach>

<script type="text/javascript">

$(function() {
	var form = $("#searchCommand");
	
	$(".action-page-submit").click(function() {
		pageSubmit($(this), $(form));
	});
});

function pageSubmit(link, form) {
	var page = $(form).find("#page");
	var size = $(form).find("#size");
	$(page).val($(link).data("page"));
	$(size).val($(link).data("size"));
	$(form).submit();
}
</script>


</body>

</html>