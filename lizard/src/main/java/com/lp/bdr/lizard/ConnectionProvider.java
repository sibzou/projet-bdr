package com.lp.bdr.lizard;

public interface ConnectionProvider {
    boolean connect(String host, String database, String username,
        String password);
}
