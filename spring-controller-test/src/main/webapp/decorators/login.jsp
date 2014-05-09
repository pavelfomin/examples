<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>

    <title><decorator:title/></title>
    <decorator:head/>

</head>
<body>
    <h1>Login decorator</h1>
    
    <div class="container">
        <div id="content">
            <decorator:body/>
        </div>
    </div>

</body>
</html>
