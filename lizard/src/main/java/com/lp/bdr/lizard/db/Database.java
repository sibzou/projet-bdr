package com.lp.bdr.lizard.db;

import com.lp.bdr.lizard.ConnectionProvider;
import com.lp.bdr.lizard.BuySellQuery;
import com.lp.bdr.lizard.WalletDistributionQuery;
import com.lp.bdr.lizard.QueryHandler;
import com.lp.bdr.lizard.QueryResult;

import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.Array;

public class Database implements ConnectionProvider, QueryHandler {
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

    private QueryResult beginQuery() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("begin dbms_output.enable(); end;");
        } catch(SQLException ignored) {}

        return new QueryResult();
    }

    public void appendDbmsOutput(QueryResult result) {
        try {
            CallableStatement cll = connection.prepareCall("declare num integer; begin num := 1000; dbms_output.get_lines(?, num); end;");
            cll.registerOutParameter(1, Types.ARRAY, "DBMSOUTPUT_LINESARRAY");
            cll.execute();

            Array array = cll.getArray(1);
            result.output = (String[])array.getArray();
        } catch(SQLException ignored) {}
    }

    @Override
    public QueryResult buy(BuySellQuery query) {
        QueryResult result = beginQuery();

        try {
            CallableStatement cstmt = connection.prepareCall("{call Acheter(?, ?, ?, ?, ?)}");
            cstmt.setInt(1, query.accountNumber);
            cstmt.setString(2, query.valueCode);
            cstmt.setString(3, query.date);
            cstmt.setInt(4, query.quantity);
            cstmt.setFloat(5, query.amount);
            cstmt.execute();
        } catch (SQLException exception) {
            result.errorMessage = exception.getMessage();
        }

        appendDbmsOutput(result);
        return result;
    }

    @Override
    public QueryResult sell(BuySellQuery query) {
        QueryResult result = beginQuery();

        try {
            CallableStatement cstmt = connection.prepareCall("{call Vendre(?, ?, ?, ?, ?)}");
            cstmt.setInt(1, query.accountNumber);
            cstmt.setString(2, query.valueCode);
            cstmt.setString(3, query.date);
            cstmt.setInt(4, query.quantity);
            cstmt.setFloat(5, query.amount);
            cstmt.execute();
        } catch (SQLException exception) {
            result.errorMessage = exception.getMessage();
        }

        appendDbmsOutput(result);
        return result;
    }

    @Override
    public QueryResult getWalletDistribution(WalletDistributionQuery query) {
        QueryResult result = new QueryResult();
        result.errorMessage = "Not implemented";
        result.output = new String[] {null};
        return result;
    }
}
