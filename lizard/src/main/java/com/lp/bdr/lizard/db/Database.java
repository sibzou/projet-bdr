package com.lp.bdr.lizard.db;

import com.lp.bdr.lizard.ConnectionProvider;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Database implements ConnectionProvider {
    private Connection connection;

    @Override
    public boolean connect(String host, String user, String password) {
        String url = "jdbc:oracle:thin:@" + host;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch(SQLException exception) {
            return false;
        }

        return true;
    }
}
