<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test Formulaire</title>
</head>
<body>
    <h1>Formulaire de test</h1>
    <form action="save-name" method="get">
        <label for="name">Votre nom :</label>
        <input type="text" id="name" name="userName" required>
        <button type="submit">Envoyer</button>
    </form>
</body>
</html> 