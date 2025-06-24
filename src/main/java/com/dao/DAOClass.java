package com.dao;

//database connections
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOClass {


	private static final String url = "jdbc:postgresql://dpg-d16hdlp5pdvs73far7jg-a.oregon-postgres.render.com/atmdatabase";

    public Connection conn;

    public DAOClass() {
        initiateDBConnection();
    }

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" PostgreSQL Driver not found: " + e);
        }
    }

    void initiateDBConnection() {
        try {
            this.conn = DriverManager.getConnection(url,"atmdatabase_user","y3kIF7Teeq8aHCVfPgxJzEkgyBw35Vtd");
            System.out.println(" DB connected successfully");
        } catch (SQLException e) {
            System.err.println(" DB connection failed: " + e);
        }
    }

    public Connection getConnection() {
        return this.conn;
    }
}
