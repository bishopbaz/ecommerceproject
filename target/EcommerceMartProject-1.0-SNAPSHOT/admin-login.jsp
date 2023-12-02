<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link rel="stylesheet" type="text/css" href="stylee.css">
</head>
<body>
<div class="container">
    <h1>Welcome to the Dashboard</h1>
    <!-- Add your dynamic content here -->
    <ul>
        <c:forEach var="product" items="${product-list}">
            <li>${product.productName} - ${product.productPrice}</li>
        </c:forEach>
    </ul>
</div>
</body>
</html>
