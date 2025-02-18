<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Résultat de la Validation</title>
    <style>
        .success {
            color: green;
            padding: 10px;
            border: 1px solid green;
            margin: 10px 0;
        }
    </style>
</head>
<body>
    <h1>Données Validées avec Succès</h1>
    
    <div class="success">
        <p><strong>Nom :</strong> ${name}</p>
        <p><strong>Âge :</strong> ${age}</p>
        <p><strong>Email :</strong> ${email}</p>
    </div>
    
    <p><a href="validation-form">Retour au formulaire</a></p>
</body>
</html> 