<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tableau de bord</title>
    <style>
        .project-list {
            margin: 20px 0;
            padding: 10px;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
    <h1>Bienvenue ${username}</h1>
    
    <div class="project-list">
        <h2>Vos projets :</h2>
        <ul>
        <% 
            List<String> projects = (List<String>) request.getAttribute("userProjects");
            if (projects != null) {
                for (String project : projects) {
        %>
                    <li><%= project %></li>
        <%
                }
            }
        %>
        </ul>
    </div>
    
    <form action="logout" method="get">
        <button type="submit">Déconnexion</button>
    </form>
</body>
</html> 