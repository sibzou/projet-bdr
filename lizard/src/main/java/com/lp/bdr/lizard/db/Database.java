package com.lp.bdr.lizard.db;

import com.lp.bdr.lizard.ConnectionProvider;
import com.lp.bdr.lizard.BuySellQuery;
import com.lp.bdr.lizard.BuySellHandlerWithError;

import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Database implements ConnectionProvider, BuySellHandlerWithError {
    private Connection connection;

    @Override
    public boolean connect(String host, String database, String user,
            String password) {

        String url = "jdbc:oracle:thin:@" + host + "/" + database;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch(SQLException exception) {
            return false;
        }

        return true;
    }

    @Override
    public String buy(BuySellQuery query) {
        try {
            CallableStatement cstmt = connection.prepareCall("{call Acheter(?, ?, ?, ?, ?)}");
            cstmt.setInt(1, query.accountNumber);
            cstmt.setString(2, query.valueCode);
            cstmt.setString(3, query.date);
            cstmt.setInt(4, query.quantity);
            cstmt.setFloat(5, query.amount);
            cstmt.execute();
        } catch (SQLException exception) {
            return "Une erreur d'achat s'est produite.";
        }

        return null;
    }

    @Override
    public String sell(BuySellQuery query) {
        try {
            CallableStatement cstmt = connection.prepareCall("{call Vendre(?, ?, ?, ?, ?)}");
            cstmt.setInt(1, query.accountNumber);
            cstmt.setString(2, query.valueCode);
            cstmt.setString(3, query.date);
            cstmt.setInt(4, query.quantity);
            cstmt.setFloat(5, query.amount);
            cstmt.execute();
        } catch (SQLException exception) {
            return "Une erreur de vente s'est produite.";
        }

        return null;
    }
}
