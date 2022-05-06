package com.lp.bdr.lizard;

public interface QueryHandler {
    String buy(BuySellQuery query);
    String sell(BuySellQuery query);
    String getWalletDistribution(WalletDistributionQuery query);
}
