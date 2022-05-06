package com.lp.bdr.lizard;

public class BuySellForm extends Form implements BuySellSwitchHandler {
    private static final int FIELD_VALUE_CODE = 0,
                             FIELD_DATE = 1,
                             FIELD_QUANTITY = 2,
                             FIELD_AMOUNT = 3;

    private BuySellToggle toggle;
    private BuySellMiddleHandler handler;

    public BuySellForm(BuySellMiddleHandler handler) {
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

    private BuySellQuery makeQuery(ErrorReporter errorReporter) {
        BuySellQuery query = new BuySellQuery();
        hideErrors();

        query.valueCode = getFieldValue(FIELD_VALUE_CODE);
        query.date = getFieldValue(FIELD_DATE);
        query.quantity = safeParseQueryInt(FIELD_QUANTITY, errorReporter);
        query.amount = safeParseQueryFloat(FIELD_AMOUNT, errorReporter);

        return query;
    }

    private void onValidate() {
        ErrorReporter errorReporter = new ErrorReporter();
        BuySellQuery query = makeQuery(errorReporter);

        if(toggle.isSellMode()) {
            handler.sell(query, errorReporter);
        } else {
            handler.buy(query, errorReporter);
        }
    }
}
