<!DOCTYPE html>
<html>
<head lang="en">
    <title>Spring Boot Demo - FreeMarker</title>
</head>
<body>
<center>
    <h1 id="title">${title}</h1>
</center>

<form method="POST" enctype="multipart/form-data" action="/file/upload">
    文件：<input type="file" name="uploadFiles" />
    <input type="submit" value="上传" />
</form>

</body>
</html>
