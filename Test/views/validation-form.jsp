<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test de Validation</title>
    <style>
        .form-group {
            margin: 10px 0;
        }
        label {
            display: inline-block;
            width: 100px;
        }
        .error-message {
            color: red;
            font-size: 0.9em;
            margin-top: 5px;
        }
        .hint {
            color: #666;
            font-size: 0.9em;
        }
    </style>
    
</head>
<body>
    <h1>Formulaire avec Validation</h1>
    
    <form action="/TestFramework/ValidationTestController/validate" method="post">
        <div class="form-group">
            <label for="name">Nom :</label>
            <input type="text" id="name" name="name" value="${param.name}">
            <span class="hint">Champ obligatoire</span>
            <c:if test="${not empty validationErrors}">
                <c:forEach items="${validationErrors}" var="error">
                    <c:if test="${error.field eq 'name'}">
                        <div class="error-message">${error.message}</div>
                    </c:if>
                </c:forEach>
            </c:if>
        </div>
        
        <div class="form-group">
            <label for="age">Âge :</label>
            <input type="number" id="age" name="age" value="${param.age}">
            <span class="hint">Entre 18 et 100 ans</span>
            <c:if test="${not empty validationErrors}">
                <c:forEach items="${validationErrors}" var="error">
                    <c:if test="${error.field eq 'age'}">
                        <div class="error-message">${error.message}</div>
                    </c:if>
                </c:forEach>
            </c:if>
        </div>
        
        <div class="form-group">
            <label for="email">Email :</label>
            <input type="text" id="email" name="email" value="${param.email}">
            <span class="hint">Format email valide requis</span>
            <c:if test="${not empty validationErrors}">
                <c:forEach items="${validationErrors}" var="error">
                    <c:if test="${error.field eq 'email'}">
                        <div class="error-message">${error.message}</div>
                    </c:if>
                </c:forEach>
            </c:if>
        </div>
        
        <button type="submit">Valider</button>
    </form>
</body>
</html> 