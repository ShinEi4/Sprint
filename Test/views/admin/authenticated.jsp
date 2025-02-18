<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <style>
        .nav { background: #f0f0f0; padding: 10px; margin-bottom: 20px; }
        .nav a { margin-right: 10px; }
        .current-user { float: right; color: #666; }
        .message { padding: 20px; background: #d4edda; margin: 20px; }
    </style>
</head>
<body>
    <%@ include file="../common/header.jsp" %>
    <div class="nav">
        <a href="../index.jsp">Accueil</a>
        <a href="all">Liste</a>
        <a href="authenticated-only">Page Authentifiée</a>
        <a href="../logout">Déconnexion</a>
        <span class="current-user">Connecté en tant que: ${sessionScope.user}</span>
    </div>
    
    <h1>${title}</h1>
    <div class="message">${message}</div>
</body>
</html> 