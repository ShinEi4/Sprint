<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion</title>
    <style>
        .error { color: red; }
        .form-group { margin: 10px 0; }
    </style>
</head>
<body>
    <h1>Connexion</h1>
    
    <% if (request.getAttribute("error") != null) { %>
        <div class="error">${error}</div>
    <% } %>
    
    <form action="login" method="post">
        <div class="form-group">
            <label for="username">Identifiant:</label>
            <input type="text" id="username" name="username" required>
        </div>
        
        <div class="form-group">
            <label for="password">Mot de passe:</label>
            <input type="password" id="password" name="password" required>
        </div>
        
        <button type="submit">Se connecter</button>
    </form>
</body>
</html> 