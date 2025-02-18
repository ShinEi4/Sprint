<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Résultat de l'upload</title>
</head>
<body>
    <h1>Fichier uploadé avec succès</h1>
    <p>Nom du fichier: ${fileName}</p>
    <p>Taille: ${fileSize} octets</p>
    
    <% if (request.getAttribute("imageData") != null) { %>
        <h2>Aperçu de l'image:</h2>
        <img src="data:image/jpeg;base64,${imageData}" alt="Image uploadée">
    <% } %>
    
    <p><a href="upload-form">Retour au formulaire</a></p>
</body>
</html> 