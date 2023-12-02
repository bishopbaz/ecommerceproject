<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.bishopbaz.ecommercemartproject.models.Product" %><%--
  Created by IntelliJ IDEA.
  User: mac
  Date: 29/11/2023
  Time: 08:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style type="text/css" >
        label {
            display: block;
        }
    </style>
    <title>PRODUCT APPLICATION</title>
</head>
<body>
<%  PrintWriter out1 = response.getWriter();
    String error =(String) request.getAttribute("error");
    if (error!=null) {
        out1.println("<html><body>");
        out1.println("<b>" +error +"</b>");
        out1.println("</body></html>");
    }
    Product editProduct = (Product) request.getAttribute("edit-product");
    if (editProduct!=null){

%>
<form method="post" action="product?edit=product">
    Product Name:<label>
    <input name="product-name" value="
<%= editProduct.getName() %>">
</label>
    Product Price:<label>
    <input name="product-price" value="
<%= editProduct.getProductPrice() %>">
</label>
    Product Quantity:<label>
    <input name="product-quantity" value="
<%= editProduct.getQuantity() %>">
</label>
    Product Category:<label>
    <input name="product-category" value="
<%= editProduct.getCategory() %>">
</label>
    <input hidden="hidden" name="id" value="
<%= editProduct.getId()%>">
    <button type="submit">Edit Product</button>

</form>

<% }
else {
%><form method="post" action="product?admin=product">
    Product Name:<label>
    <input name="product-name">
</label>
    Product Price:<label>
    <input name="product-price">
</label>
    Product Quantity:<label>
    <input name="product-quantity">
</label>
    Product Category:<label>
    <input name="product-category">
</label>
    <button type="submit">Add Product</button>

</form>
<%}%>
<a href="product?admin=view-product">View Product</a>
</body>
</html>
