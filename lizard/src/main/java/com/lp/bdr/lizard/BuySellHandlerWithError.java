package com.lp.bdr.lizard;

public interface BuySellHandlerWithError {
    String buy(BuySellQuery query);
    String sell(BuySellQuery query);
}
