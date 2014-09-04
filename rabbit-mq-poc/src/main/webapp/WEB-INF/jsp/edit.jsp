<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html> 

<head>
</head>

<body>
<h1>Rabbit MQ POC Edit</h1>

<div class="form">
	<form action="show" method="post">

		<input type="text" name="cid" id="cid" size="40" maxlength="64" value="${cid}">

		<div>	
			<input type="submit" />
		</div>
	</form>
	
</div>

</body>
</html>