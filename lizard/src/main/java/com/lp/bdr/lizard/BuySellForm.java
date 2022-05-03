package com.lp.bdr.lizard;

public class BuySellForm extends Form implements BuySellSwitchHandler {
    private static final int FIELD_VALUE_CODE = 0,
                             FIELD_DATE = 1,
                             FIELD_QUANTITY = 2,
                             FIELD_AMOUNT = 3;

    public BuySellForm() {
        super("Acheter ou vendre");
        addField(FIELD_VALUE_CODE, "Code valeur");
        addField(FIELD_DATE, "Date");
        addField(FIELD_QUANTITY, "Quantité");
        addField(FIELD_AMOUNT, "Montant");

        BuySellToggle toggle = new BuySellToggle(this);
        getChildren().add(0, toggle);
    }

    @Override
    public void onBuyMode() {
        setValidateButtonText("Acheter");
    }

    @Override
    public void onSellMode() {
        setValidateButtonText("Vendre");
    }
}
