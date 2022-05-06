package com.lp.bdr.lizard;

public interface QueryHandler {
    QueryResult buy(BuySellQuery query);
    QueryResult sell(BuySellQuery query);
    QueryResult getWalletDistribution(WalletDistributionQuery query);
}
