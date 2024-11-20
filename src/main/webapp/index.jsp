<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calculator Application</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background: #f0f0f0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
        }
        h1 {
            color: #333;
            font-size: 32px;
            margin-bottom: 20px;
            text-align: center;
        }
        form {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }
        input[type="text"] {
            width: 70%;
            padding: 12px;
            font-size: 16px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
        }
        button {
            padding: 12px 20px;
            font-size: 16px;
            color: #fff;
            background-color: #007BFF;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #0056b3;
        }
        .result, .history {
            margin-top: 30px;
        }
        .history ul {
            list-style-type: none;
            padding: 0;
        }
        .history li {
            background-color: #f9f9f9;
            padding: 12px;
            margin-bottom: 10px;
            border-radius: 5px;
            border: 1px solid #ddd;
        }
        .history li span {
            color: #555;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Calculator</h1>
        <form action="calc" method="post">
            <input type="text" name="input" placeholder="Enter expression" required>
            <button type="submit">Calculate</button>
        </form>

        <c:if test="${not empty result}">
            <h3>Result: ${result}</h3>
        </c:if>

        <c:if test="${not empty error}">
            <h3 style="color: red;">${error}</h3>
        </c:if>

        <h3>Calculation History</h3>
        <ul>
            <c:forEach var="calc" items="${history}">
                <li>${calc.input} = ${calc.result} at ${calc.timestamp}</li>
            </c:forEach>
        </ul>
    </div>
</body>
</html>
