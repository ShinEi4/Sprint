<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test des erreurs</title>
</head>
<body>
    <h1>Page de test</h1>
    <p>${message}</p>
    
    <h2>Tests d'erreurs disponibles :</h2>
    <ul>
        <li><a href="${pageContext.request.contextPath}/ErrorTestController/all">Test URL dupliquée</a></li>
        <li><a href="${pageContext.request.contextPath}/ErrorTestController/invalid-return">Test type de retour invalide</a></li>
        <li><a href="${pageContext.request.contextPath}/ErrorTestController/not-exist">Test URL inexistante</a></li>
        <li><a href="${pageContext.request.contextPath}/ErrorTestController/valid">Test méthode valide (ModelView)</a></li>
        <li><a href="${pageContext.request.contextPath}/ErrorTestController/valid-string">Test méthode valide (String)</a></li>
    </ul>
</body>
</html> 