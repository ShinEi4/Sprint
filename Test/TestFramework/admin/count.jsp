<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <style>
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
    
    <p>Nombre total d'utilisateurs : ${count}</p>
    
    <p><a href="all">Voir la liste des utilisateurs</a></p>
    <p><a href="details">Voir les détails des utilisateurs</a></p>
</body>
</html> 