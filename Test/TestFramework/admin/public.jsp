<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <style>
        .nav { background: #f0f0f0; padding: 10px; margin-bottom: 20px; }
        .nav a { margin-right: 10px; }
        .message { padding: 20px; background: #e8f4ff; margin: 20px; }
    </style>
</head>
<body>
    <%@ include file="../common/header.jsp" %>
    <div class="nav">
        <a href="../index.jsp">Accueil</a>
        <a href="all">Liste</a>
        <a href="public-info">Info Publique</a>
        <a href="authenticated-only">Page Authentifiée</a>
    </div>
    
    <h1>${title}</h1>
    <div class="message">${message}</div>
</body>
</html> 