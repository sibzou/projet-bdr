package com.lp.bdr.lizard;

public class BuySellForm extends Form implements BuySellSwitchHandler {
    private static final int FIELD_VALUE_CODE = 0,
                             FIELD_DATE = 1,
                             FIELD_QUANTITY = 2,
                             FIELD_AMOUNT = 3;

    private BuySellToggle toggle;
    private BuySellHandler handler;

    public BuySellForm(BuySellHandler handler) {
        super("Acheter ou vendre");
        this.handler = handler;

        addField(FIELD_VALUE_CODE, "Code valeur");
        addField(FIELD_DATE, "Date");
        addField(FIELD_QUANTITY, "Quantité");
        addField(FIELD_AMOUNT, "Montant");

        toggle = new BuySellToggle(this);
        getChildren().add(0, toggle);

        setOnValidate(this::onValidate);
    }

    @Override
    public void onBuyMode() {
        setValidateButtonText("Acheter");
    }

    @Override
    public void onSellMode() {
        setValidateButtonText("Vendre");
    }

    private void onValidate() {
        if(toggle.isSellMode()) {
            handler.sell(null);
        } else {
            handler.buy(null);
        }
    }
}
