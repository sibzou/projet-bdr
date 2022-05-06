package com.lp.bdr.lizard;

public interface BuySellMiddleHandler {
    void buy(BuySellQuery query, ErrorReporter errorReporter);
    void sell(BuySellQuery query, ErrorReporter errorReporter);
}
