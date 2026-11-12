<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Casino - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<main class="card">
    <h1>Casino</h1>
    <p class="hint">Enter a username to start playing.</p>
    <p class="error">${error}</p>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <label for="username">Username</label>
        <input id="username" name="username" maxlength="30" pattern="[A-Za-z0-9_]+" required autofocus>
        <button type="submit">Play</button>
    </form>
</main>
</body>
</html>
