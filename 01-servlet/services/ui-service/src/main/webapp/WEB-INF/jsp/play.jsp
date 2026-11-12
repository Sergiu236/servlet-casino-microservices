<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Casino - Slots</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<main class="card">
    <h1>Slots</h1>
    <p>Player: <strong>${sessionScope.username}</strong></p>
    <p>Balance: <strong>${account.balance}</strong> chips</p>
    <p class="error">${error}</p>
    <form method="post" action="${pageContext.request.contextPath}/play">
        <label for="bet">Bet</label>
        <input id="bet" name="bet" type="number" min="1" value="10" required>
        <button type="submit">Spin</button>
    </form>
    <form method="post" action="${pageContext.request.contextPath}/logout">
        <button type="submit" class="secondary">Logout</button>
    </form>
</main>
</body>
</html>
