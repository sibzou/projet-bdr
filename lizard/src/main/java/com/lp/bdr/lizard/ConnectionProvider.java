package com.lp.bdr.lizard;

public interface ConnectionProvider {
    boolean connect(String host, String username, String password);
}
