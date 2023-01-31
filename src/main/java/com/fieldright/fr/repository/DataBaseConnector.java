package com.fieldright.fr.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {


    private final String URL = "jdbc:postgresql://ec2-52-201-55-4.compute-1.amazonaws.com:5432/d4tunj57t7g0p8";
    private final String USER = "knhaymsopspupn";
    private final String PASSWORD = "0c87e8076405a61952efe85a14f523259b0a9fc229c703dc10198564f1604831";

    public Connection connect() {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("VendorError: " + e.getErrorCode());
            return null;
        } catch (Exception e) {
            System.err.println("Falha ao conectar com o banco de dados: " + e);
            return null;
        }
    }
}
