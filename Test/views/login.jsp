<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion</title>
    <style>
        .error { color: red; }
        .form-group { margin: 10px 0; }
        .info { 
            background: #f8f9fa;
            padding: 15px;
            margin: 20px 0;
            border: 1px solid #ddd;
        }
        .user-info {
            margin: 5px 0;
            padding: 5px;
            border-bottom: 1px solid #eee;
        }
    </style>
</head>
<body>
    <h1>Connexion</h1>
    
    <div class="info">
        <h3>Utilisateurs de test :</h3>
        <div class="user-info">
            <strong>Admin :</strong> john / pass123
            <br>
            <small>Rôles : ADMIN, USER_MANAGER</small>
        </div>
        <div class="user-info">
            <strong>Manager :</strong> alice / pass456
            <br>
            <small>Rôle : USER_MANAGER</small>
        </div>
        <div class="user-info">
            <strong>Utilisateur :</strong> bob / pass789
            <br>
            <small>Rôle : USER</small>
        </div>
    </div>
    
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