package com.lp.bdr.lizard.db;

import com.lp.bdr.lizard.ConnectionProvider;
import com.lp.bdr.lizard.BuySellQuery;
import com.lp.bdr.lizard.BuySellHandlerWithError;

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
        return "Pas de base de données pour l'achat.";
    }

    @Override
    public String sell(BuySellQuery query) {
        return "Pas de base de données pour la vente.";
    }
}
