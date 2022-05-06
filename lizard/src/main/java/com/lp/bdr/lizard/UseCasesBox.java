package com.lp.bdr.lizard;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class UseCasesBox extends HBox {
    private static final int FIELD_CRITERIA = 0;

    private Form walletDistributionForm;
    private WalletDistributionMiddleProvider walletDistributionMiddleProvider;

    public UseCasesBox(MainBox mainBox) {
        BuySellForm buySellForm = new BuySellForm(mainBox);
        walletDistributionMiddleProvider = mainBox;

        walletDistributionForm = new Form("Calculer");
        walletDistributionForm.addField(FIELD_CRITERIA, "Critère");

        walletDistributionForm
            .setOnValidate(this::onWalletDistributionFormValidate);

        getChildren().addAll(buySellForm, walletDistributionForm);

        setMaxWidth(800);
        setSpacing(Main.MARGIN);
        setHgrow(buySellForm, Priority.ALWAYS);
        setHgrow(walletDistributionForm, Priority.ALWAYS);
    }

    private void onWalletDistributionFormValidate() {
        WalletDistributionQuery query = new WalletDistributionQuery();
        query.criteria = walletDistributionForm.getFieldValue(FIELD_CRITERIA);

        walletDistributionMiddleProvider.getWalletDistribution(query);
    }
}
