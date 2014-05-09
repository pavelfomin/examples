<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>

	<!-- styles -->
	<link href="<c:url value="/resources/css/main/main.css"/>" rel="stylesheet"/>

	<!-- scripts -->
	<script type="text/javascript" src="<c:url value="/resources/js/main/main.js"/>"></script>
	
    <title><decorator:title/></title>
    <decorator:head/>

</head>
<body>
    <h1>Main decorator</h1>
	<header>Main decorator header</header>
	<hr/>

    <div class="container">
        <div id="content">
            <decorator:body/>
        </div>
    </div>

	<hr/>
	<footer>Main decorator footer</footer>
	<div>Application version: <spring:message code="application.version" text="N/A"/>
	</div>
</body>
</html>
