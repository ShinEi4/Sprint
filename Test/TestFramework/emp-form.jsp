<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Formulaire Employé</title>
    <style>
        .form-group {
            margin: 10px 0;
        }
        label {
            display: inline-block;
            width: 100px;
        }
    </style>
</head>
<body>
    <h1>Ajouter un employé</h1>
    <form action="save-emp" method="get">
        <div class="form-group">
            <label for="nom">Nom :</label>
            <input type="text" id="nom" name="nom" required>
        </div>
        
        <div class="form-group">
            <label for="age">Age :</label>
            <input type="number" id="age" name="age" required>
        </div>
        
        <div class="form-group">
            <label for="salaire">Salaire :</label>
            <input type="number" id="salaire" name="salaire" step="0.01" required>
        </div>
        
        <button type="submit">Enregistrer</button>
    </form>
</body>
</html> 