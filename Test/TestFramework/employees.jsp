<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <style>
        .employee-list {
            margin: 20px;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .employee-item {
            margin: 10px 0;
            padding: 10px;
            background-color: #f5f5f5;
        }
    </style>
</head>
<body>
    <h1>${title}</h1>
    
    <div class="employee-list">
        <h2>Nombre total d'employés : ${count}</h2>
        
        <h3>Liste des employés :</h3>
        <% 
            String[] names = (String[]) request.getAttribute("names");
            if (names != null) {
                for (String name : names) {
        %>
                    <div class="employee-item">
                        <%= name %>
                    </div>
        <%
                }
            }
        %>
    </div>
    
    <p><a href="${pageContext.request.contextPath}/EmpController/all">Retour à la liste simple</a></p>
</body>
</html> 