package com.lp.bdr.lizard;

public interface BuySellHandler {
    void buy(BuySellQuery query, ErrorReporter errorReporter);
    void sell(BuySellQuery query, ErrorReporter errorReporter);
}
