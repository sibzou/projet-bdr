package com.lp.bdr.lizard;

import com.lp.bdr.lizard.db.Database;

import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MainBox extends VBox implements BuySellHandler {
    private LabeledTextField accountTextField;
    private BuySellHandlerWithError buySellHandler;

    public MainBox(Database database) {
        AuthBox authBox = new AuthBox(database);

        accountTextField = new LabeledTextField("Numéro de compte");
        accountTextField.setMaxWidth(400);

        UseCasesBox useCasesBox = new UseCasesBox(this);
        ResultBox resultBox = new ResultBox();
        getChildren().addAll(authBox, accountTextField, useCasesBox, resultBox);

        setPadding(new Insets(Main.MARGIN));
        setAlignment(Pos.TOP_CENTER);

        setMargin(authBox, new Insets(0, 0, 4 * Main.MARGIN, 0));
        setMargin(accountTextField, new Insets(0, 0, 2 * Main.MARGIN, 0));
        setMargin(useCasesBox, new Insets(0, 0, 4 * Main.MARGIN, 0));

        buySellHandler = database;
    }

    @Override
    public void buy(BuySellQuery query, ErrorReporter errorReporter) {
        accountTextField.hideError();
        query.accountNumber = accountTextField.safeParseInt(errorReporter);

        if(!errorReporter.error)
            buySellHandler.buy(query);
    }

    @Override
    public void sell(BuySellQuery query, ErrorReporter errorReporter) {
        accountTextField.hideError();
        query.accountNumber = accountTextField.safeParseInt(errorReporter);

        if(!errorReporter.error)
            buySellHandler.sell(query);
    }
}
