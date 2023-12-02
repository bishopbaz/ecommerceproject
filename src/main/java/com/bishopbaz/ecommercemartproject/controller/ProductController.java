package com.bishopbaz.ecommercemartproject.controller;

import com.bishopbaz.ecommercemartproject.dao.OrderDao;
import com.bishopbaz.ecommercemartproject.dao.ProductDao;
import com.bishopbaz.ecommercemartproject.dao.UserDao;
import com.bishopbaz.ecommercemartproject.models.Cart;
import com.bishopbaz.ecommercemartproject.models.Order;
import com.bishopbaz.ecommercemartproject.models.Product;
import com.bishopbaz.ecommercemartproject.models.Users;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "product", value = "/product")
public class ProductController extends HttpServlet {

    private ProductDao productDao = new ProductDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String viewProduct = req.getParameter("admin");
        String editProduct = req.getParameter("edit");
        String deleteProduct = req.getParameter("delete");
        String buyproduct = req.getParameter("buy");
        String payment = req.getParameter("payment");
        Cart cart = new Cart();
        Order makeOrderPayment = (Order) req.getSession().getAttribute("order");
        Cart paymentCart = (Cart) req.getSession().getAttribute("cart");

        if (makeOrderPayment!=null){
            UserDao userDao = new UserDao();
            OrderDao orderDao = new OrderDao();
            Long id = (Long) req.getSession().getAttribute("userID");
            Users user = userDao.findUserById.apply(id);
            if (user.getBalance().doubleValue()>makeOrderPayment.getTotalPrice().doubleValue()){
                BigDecimal balance = user.getBalance().subtract(makeOrderPayment.getTotalPrice());
                System.out.println(balance);
                user.setBalance(balance);
                userDao.updateUserBalance.apply(user);
                orderDao.saveOrder.apply(makeOrderPayment);
                req.setAttribute("paid", "Payment made successfully, your product will be delivered shortly!");
                req.getSession().setAttribute("order", null);
                RequestDispatcher dispatcher = req.getRequestDispatcher("payment-successful.jsp");
                dispatcher.forward(req, resp);
            }
            else {
                req.getSession().setAttribute("order", null);
                req.setAttribute("paid", "Insufficient Balance in your account!");
                RequestDispatcher dispatcher = req.getRequestDispatcher("payment-successful.jsp");
                dispatcher.forward(req, resp);
            }
        }
        if (payment!=null&&paymentCart!=null){
            Order order = new Order();
            final BigDecimal[] totalPrice = {new BigDecimal(0)};
            List<Product> productList = new ArrayList<>();
            paymentCart.getProductIds().forEach(id->{
                order.setProductIds((order.getProductIds() != null ? order.getProductIds()+"," : "") + id);
                Product product = productDao.findProductById.apply(id);
                //to prevent multithreading problems primitive array is used here is primitive datatype are thread safe
                totalPrice[0] = totalPrice[0].add(product.getProductPrice());
                productList.add(product);
            });
            HttpSession orderSession = req.getSession();
            order.setTotalPrice(totalPrice[0]);
            orderSession.setAttribute("order", order);
            req.setAttribute("product-list", productList);
            RequestDispatcher dispatcher = req.getRequestDispatcher("payment.jsp");
            dispatcher.forward(req, resp);
        }
        if (buyproduct!=null){
            if (req.getSession().getAttribute("cart")!=null){
                cart = (Cart) req.getSession().getAttribute("cart");
                cart.getProductIds().add(Long.parseLong(buyproduct));
            }
            else {
                HttpSession cartSession = req.getSession();
                Long id = (Long) cartSession.getAttribute("userID");
                cart.setUserId(id);
                List<Long> productIds = new ArrayList<>();
                productIds.add(Long.parseLong(buyproduct));
                cart.setProductIds(productIds);
                cartSession.setAttribute("cart", cart);
            }
            System.out.println(cart);
            List<Product> productList = productDao.findAllProducts.get();
            req.setAttribute("product-list", productList);
            RequestDispatcher dispatcher = req.getRequestDispatcher("dashboard.jsp");
            dispatcher.forward(req, resp);
        }
        if(editProduct!=null){
            Product product =  productDao.findProductById.apply(Long.parseLong(editProduct));
            req.setAttribute("edit-product", product);
            RequestDispatcher dispatcher = req.getRequestDispatcher("admin.jsp");
            dispatcher.forward(req, resp);
        }
        if (viewProduct!=null){
            List<Product> productList = productDao.findAllProducts.get();
            req.setAttribute("product-list", productList);
            RequestDispatcher dispatcher = req.getRequestDispatcher("admin-view-product.jsp");
            dispatcher.forward(req, resp);
        }
        if (req.getAttribute("shop")!=null) {
            List<Product> productList = productDao.findAllProducts.get();
            req.setAttribute("product-list", productList);
            RequestDispatcher dispatcher = req.getRequestDispatcher("dashboard.jsp");
            dispatcher.forward(req, resp);
        }
        List<Product> productList = productDao.findAllProducts.get();
        req.setAttribute("product-list", productList);
        RequestDispatcher dispatcher = req.getRequestDispatcher("dashboard.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String adminAddProduct = req.getParameter("admin");
        String editProduct = req.getParameter("edit");
        String id = req.getParameter("id");
        if (editProduct!=null){  String productName = req.getParameter("product-name");
            String productPrice = req.getParameter("product-price");
            String productQuantity = req.getParameter("product-quantity");
            String productCategory = req.getParameter("product-category");
            Product product = new Product(productName, productCategory, Long.parseLong(productQuantity), new BigDecimal(productPrice));
            product.setId(Long.parseLong(id));
            productDao.updateProduct.apply(product);
            resp.sendRedirect("product?admin=view-product");
        }
        if (adminAddProduct!=null){
            String productName = req.getParameter("product-name");
            String productPrice = req.getParameter("product-price");
            String productQuantity = req.getParameter("product-quantity");
            String productCategory = req.getParameter("product-category");
            Product product = new Product(productName, productCategory, Long.parseLong(productQuantity), new BigDecimal(productPrice));
            if (!productDao.saveProduct.apply(product)){
                List<Product> productList = productDao.findAllProducts.get();
                req.setAttribute("product-list", productList);
                RequestDispatcher dispatcher = req.getRequestDispatcher("admin-view-product.jsp");
                dispatcher.forward(req, resp);
            }
            else{
                req.setAttribute("error", "Could not add product");
                RequestDispatcher dispatcher = req.getRequestDispatcher("admin.jsp");
                dispatcher.forward(req, resp);
            }
        }
    }
}
