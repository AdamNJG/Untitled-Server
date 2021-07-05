<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Spring Boot Email using FreeMarker</title>
</head>
<body>
<div style="margin-top: 10px">Greetings, <b>${user.username}</b></div>
<div>Please validate your email address using <a href="http://localhost:3000/validate/${token}">this link</a></div>
<br/>
<div> Have a nice day..!</div>
</body>
</html>