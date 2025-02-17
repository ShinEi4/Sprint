<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Détails Employé</title>
    <style>
        .employee-details {
            margin: 20px;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .success-message {
            color: green;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <h1>Détails de l'employé</h1>
    
    <div class="success-message">${message}</div>
    
    <div class="employee-details">
        <h2>Informations enregistrées :</h2>
        <p><strong>Nom :</strong> ${employee.nom}</p>
        <p><strong>Age :</strong> ${employee.age} ans</p>
        <p><strong>Salaire :</strong> ${employee.salaire} €</p>
    </div>
    
    <p><a href="emp-form">Retour au formulaire</a></p>
</body>
</html> 