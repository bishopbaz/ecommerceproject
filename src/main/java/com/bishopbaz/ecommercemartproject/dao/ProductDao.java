package com.bishopbaz.ecommercemartproject.dao;

import com.bishopbaz.ecommercemartproject.config.DatabaseConfiguration;
import com.bishopbaz.ecommercemartproject.models.Product;
import com.bishopbaz.ecommercemartproject.service.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class ProductDao {

    private Logger logger = Logger.getGlobal();
    private Connection connection;

    public UserService connect = () -> {
        Class.forName("com.mysql.cj.jdbc.Driver");
        DatabaseConfiguration configuration = new DatabaseConfiguration();
        Properties properties = new Properties();
        properties.setProperty("User", configuration.getDB_URL());
        properties.setProperty("Password", configuration.getDB_PASSWORD());
        if (connection==null|| connection.isClosed()){
            connection = DriverManager.getConnection(configuration.getDB_URL(), configuration.getDB_USERNAME(), configuration.getDB_PASSWORD());
        }
    };


    public Function<Product, Boolean> saveProduct = (product -> {
        try {
            connect.compile();
        } catch (SQLException | ClassNotFoundException e) {
            logger.warning("SQL exception"+e.getMessage());
            throw new RuntimeException(e);
        }
        String query = "INSERT INTO ProductDB.product  (name, price, quantity, category) VALUES (?, ?, ?, ?) ";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getProductPrice().doubleValue());
            preparedStatement.setLong(3, product.getQuantity());
            preparedStatement.setString(4, product.getCategory());
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    });

    public Function<Product, Boolean> updateProduct = (product -> {
        try {
            connect.compile();
        } catch (SQLException | ClassNotFoundException e) {
            logger.warning("SQL exception"+e.getMessage());
            throw new RuntimeException(e);
        }
        String query = "UPDATE ProductDB.product SET name =?, price=?, quantity=?, category=? WHERE id =? ";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getProductPrice().doubleValue());
            preparedStatement.setLong(3, product.getQuantity());
            preparedStatement.setString(4, product.getCategory());
            preparedStatement.setLong(5, product.getId());
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    });


    public Supplier<List<Product>> findAllProducts = () ->{
        try {
            connect.compile();
        } catch (SQLException | ClassNotFoundException e) {
            logger.warning("SQL exception"+e.getMessage());
            throw new RuntimeException(e);
        }
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM ProductDB.product";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                 Product product = Product.builder()
                         .id(resultSet.getLong(1))
                         .name(resultSet.getString(2))
                         .category(resultSet.getString(3))
                         .productPrice(resultSet.getBigDecimal(5))
                         .quantity(resultSet.getLong(4)).build();
                 productList.add(product);
            }
            return productList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    public Function<Long, Product> findProductById = (id) ->{
        try {
            connect.compile();
        } catch (SQLException | ClassNotFoundException e) {
            logger.warning("SQL exception"+e.getMessage());
            throw new RuntimeException(e);
        }
        String query = "SELECT * FROM ProductDB.product where id = ?";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Product product = Product.builder()
                        .id(resultSet.getLong(1))
                        .name(resultSet.getString(2))
                        .category(resultSet.getString(3))
                        .productPrice(resultSet.getBigDecimal(5))
                        .quantity(resultSet.getLong(4)).build();
               return product;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

}
