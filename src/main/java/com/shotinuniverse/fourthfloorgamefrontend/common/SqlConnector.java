package com.shotinuniverse.fourthfloorgamefrontend.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlConnector {

    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;

    public static void openConnection() throws ClassNotFoundException, SQLException
    {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + SessionManager.dbPath);

        statement = connection.createStatement();
    }

    public static void closeConnection() throws ClassNotFoundException, SQLException
    {
        connection.close();
        statement.close();
        resultSet.close();
    }
}
