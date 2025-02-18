<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <style>
        .user-list { margin: 20px; }
        .user-item { padding: 10px; border-bottom: 1px solid #ccc; }
        .nav { background: #f0f0f0; padding: 10px; margin-bottom: 20px; }
        .nav a { margin-right: 10px; }
        .current-user { float: right; color: #666; }
    </style>
</head>
<body>
    <%@ include file="../common/header.jsp" %>
    <div class="nav">
        <a href="../index.jsp">Accueil</a>
        <a href="all">Liste</a>
        <a href="count">Compteur</a>
        <a href="details">Détails</a>
        <a href="../logout">Déconnexion</a>
        <span class="current-user">Connecté en tant que: ${sessionScope.user}</span>
    </div>
    
    <h1>${title}</h1>
    
    <div class="user-list">
        <c:forEach items="${users}" var="user">
            <div class="user-item">
                ${user}
            </div>
        </c:forEach>
    </div>
    
    <p><a href="count">Voir le nombre d'utilisateurs</a></p>
    <p><a href="details">Voir les détails des utilisateurs</a></p>
</body>
</html> 