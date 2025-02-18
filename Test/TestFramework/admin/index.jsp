<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test des Autorisations</title>
    <style>
        .section {
            margin: 20px;
            padding: 15px;
            border: 1px solid #ddd;
        }
        .success-link { color: green; }
        .error-link { color: red; }
        .note { font-size: 0.9em; color: #666; }
        .current-user {
            background: #e8f4ff;
            padding: 10px;
            margin-bottom: 20px;
        }
        .role-tag {
            display: inline-block;
            background: #eee;
            padding: 2px 8px;
            border-radius: 3px;
            margin: 0 3px;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
    <%@ include file="../common/header.jsp" %>

    <h1>Tests des Autorisations</h1>
    
    <div class="section">
        <h2>1. Pages Publiques</h2>
        <ul>
            <li><a href="/TestFramework/AdminController/public-info" class="success-link">Page publique (accessible à tous)</a></li>
            <li><a href="/TestFramework/AuthController/logout" class="success-link">Se déconnecter</a></li>
        </ul>
    </div>

    <div class="section">
        <h2>2. Pages avec Authentification uniquement</h2>
        <ul>
            <li><a href="/TestFramework/AdminController/authenticated-only" class="success-link">Page authentifiée (tous utilisateurs connectés)</a></li>
        </ul>
        <p class="note">✓ Nécessite uniquement d'être connecté, pas de rôle spécifique requis</p>
    </div>

    <div class="section">
        <h2>3. Pages Admin</h2>
        <ul>
            <li><a href="/TestFramework/AdminController/all" class="success-link">Liste des utilisateurs (ADMIN uniquement)</a></li>
            <li><a href="/TestFramework/AdminController/count" class="success-link">Nombre d'utilisateurs (ADMIN uniquement)</a></li>
        </ul>
        <p class="note">✓ Nécessite le rôle ADMIN</p>
    </div>

    <div class="section">
        <h2>4. Pages Manager</h2>
        <ul>
            <li><a href="/TestFramework/AdminController/manager-only" class="success-link">Page Manager (USER_MANAGER uniquement)</a></li>
            <li><a href="/TestFramework/AdminController/details" class="success-link">Détails des utilisateurs (ADMIN ou USER_MANAGER)</a></li>
        </ul>
        <p class="note">✓ Nécessite le rôle USER_MANAGER ou ADMIN selon la page</p>
    </div>

    <div class="section">
        <h2>5. Tests d'Erreurs</h2>
        <ul>
            <li><a href="/TestFramework/AdminController/invalid" class="error-link">Page inexistante</a></li>
            <li><a href="/TestFramework/AdminController/super-admin" class="error-link">Page avec rôle inexistant</a></li>
        </ul>
        <p class="note">⚠ Ces liens devraient générer des erreurs</p>
    </div>

    <div class="section">
        <h2>Guide de Test</h2>
        <ol>
            <li>Connectez-vous avec différents utilisateurs pour tester les accès :</li>
            <ul>
                <li><strong>john</strong> (ADMIN + USER_MANAGER) : Accès à toutes les pages</li>
                <li><strong>alice</strong> (USER_MANAGER) : Accès aux pages manager et authentifiées</li>
                <li><strong>bob</strong> (USER) : Accès uniquement aux pages authentifiées</li>
            </ul>
            <li>Testez la déconnexion et tentez d'accéder aux pages protégées</li>
            <li>Vérifiez que les redirections fonctionnent correctement</li>
        </ol>
    </div>
</body>
</html> 