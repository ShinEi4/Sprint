<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    </style>
</head>
<body>
    <h1>Formulaire avec Validation</h1>
    
    <form action="/TestFramework/ValidationTestController/validate" method="post">
        <div class="form-group">
            <label for="name">Nom :</label>
            <input type="text" id="name" name="name">
            <span class="hint">Champ obligatoire</span>
        </div>
        
        <div class="form-group">
            <label for="age">Âge :</label>
            <input type="number" id="age" name="age">
            <span class="hint">Entre 18 et 100 ans</span>
        </div>
        
        <div class="form-group">
            <label for="email">Email :</label>
            <input type="text" id="email" name="email">
            <span class="hint">Format email valide requis</span>
        </div>
        
        <button type="submit">Valider</button>
    </form>
</body>
</html> 