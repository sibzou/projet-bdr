package com.lp.bdr.lizard;

import com.lp.bdr.lizard.db.Database;

import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MainBox extends VBox implements BuySellMiddleHandler,
        WalletDistributionMiddleProvider {

    private LabeledTextField accountTextField;
    private QueryHandler queryHandler;
    private ResultBox resultBox;

    public MainBox(Database database) {
        AuthBox authBox = new AuthBox(database);

        accountTextField = new LabeledTextField("Numéro de compte");
        accountTextField.setMaxWidth(400);

        UseCasesBox useCasesBox = new UseCasesBox(this);
        resultBox = new ResultBox();
        getChildren().addAll(authBox, accountTextField, useCasesBox, resultBox);

        setPadding(new Insets(Main.MARGIN));
        setAlignment(Pos.TOP_CENTER);

        setMargin(authBox, new Insets(0, 0, 4 * Main.MARGIN, 0));
        setMargin(accountTextField, new Insets(0, 0, 2 * Main.MARGIN, 0));
        setMargin(useCasesBox, new Insets(0, 0, 4 * Main.MARGIN, 0));

        queryHandler = database;
    }

    private <Q extends Query> void doQuery(Q query, ErrorReporter errorReporter,
            QueryPerformer<Q> performer) {

        accountTextField.hideError();
        query.accountNumber = accountTextField.safeParseInt(errorReporter);

        if(errorReporter.error) return;
        QueryResult result = performer.perform(query);
        resultBox.reset();

        if(result.errorMessage == null)
            resultBox.showSuccess();
        else
            resultBox.showError(result.errorMessage);

        resultBox.showOutput(result.output);
    }

    @Override
    public void buy(BuySellQuery query, ErrorReporter errorReporter) {
        doQuery(query, errorReporter, queryHandler::buy);
    }

    @Override
    public void sell(BuySellQuery query, ErrorReporter errorReporter) {
        doQuery(query, errorReporter, queryHandler::sell);
    }

    @Override
    public void getWalletDistribution(WalletDistributionQuery query) {
        ErrorReporter errorReporter = new ErrorReporter();
        doQuery(query, errorReporter, queryHandler::getWalletDistribution);
    }
}
