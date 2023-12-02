<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.bishopbaz.ecommercemartproject.models.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.codehaus.jackson.map.ObjectMapper" %><%--
  Created by IntelliJ IDEA.
  User: mac
  Date: 29/11/2023
  Time: 08:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>PRODUCT APPLICATION</title></head>
<body bgcolor="aqua">
<%  PrintWriter out1 = response.getWriter();

    out1.println("<table border="+1+">\n" +
            "  <thead> <tr><th>Name </th>  <th> Quantity  </th>   <th> Price </th><th> Edit/Delete </th></tr></thead>");
    List<Product> productList = (List<Product>) request.getAttribute("product-list");
    productList.forEach(product -> {
        out1.println(
                "<tr><td>"+
                        product.getName()+" "+
                        "</td><td>"+
                        product.getQuantity()+" "+
                        "</td><td>"+
                        product.getProductPrice()+""+ "</td><td>"
        );
        out1.println("<a href ='product?edit="+ product.getId()+"'>Edit</a>");
        out1.println("<a href ='product?delete="+ product.getId()+"'>Delete</a>");
        out1.println("</td></tr>");
    });
    out1.println("\n" +
            "</table>");
%>
</body>
</html>
