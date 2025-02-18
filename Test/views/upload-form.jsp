<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Upload de fichier</title>
</head>
<body>
    <h1>Upload de fichier</h1>
    <form action="/TestFramework/FileUploadController/upload" method="post" enctype="multipart/form-data">
        <div>
            <label for="file">Sélectionner un fichier:</label>
            <input type="file" id="file" name="file" required>
        </div>
        <button type="submit">Uploader</button>
    </form>
</body>
</html> 