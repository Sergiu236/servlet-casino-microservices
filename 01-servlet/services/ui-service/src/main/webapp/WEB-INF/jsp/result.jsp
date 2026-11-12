<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Casino - Result</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<main class="card">
    <h1>${result.win ? 'You won!' : 'You lost'}</h1>
    <div class="reels">
        <span>${result.reels[0]}</span>
        <span>${result.reels[1]}</span>
        <span>${result.reels[2]}</span>
    </div>
    <p>Bet: <strong>${result.bet}</strong> chips</p>
    <p>Payout: <strong>${result.payout}</strong> chips</p>
    <p>New balance: <strong>${result.balance}</strong> chips</p>
    <a class="button" href="${pageContext.request.contextPath}/play">Play again</a>
</main>
</body>
</html>
