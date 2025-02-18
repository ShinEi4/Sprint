<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <style>
        .user-details { margin: 20px; }
        .user-detail { 
            padding: 15px;
            margin: 10px 0;
            border: 1px solid #ddd;
            background: #f9f9f9;
        }
        .roles { 
            color: #666;
            margin-top: 5px;
        }
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
    
    <div class="user-details">
        <c:forEach items="${users}" var="user">
            <div class="user-detail">
                <strong>${user}</strong>
                <div class="roles">
                    Rôles : 
                    <c:forEach items="${roles[user]}" var="role" varStatus="status">
                        ${role}${!status.last ? ', ' : ''}
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </div>
    
    <p><a href="all">Voir la liste des utilisateurs</a></p>
    <p><a href="count">Voir le nombre d'utilisateurs</a></p>
</body>
</html> 