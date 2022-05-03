package com.lp.bdr.lizard;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class UseCasesBox extends HBox {
    private static final int FIELD_CRITERIA = 0;

    public UseCasesBox() {
        BuySellForm buySellForm = new BuySellForm();

        Form distributionForm = new Form("Calculer");
        distributionForm.addField(FIELD_CRITERIA, "Critère");

        getChildren().addAll(buySellForm, distributionForm);

        setMaxWidth(800);
        setSpacing(Main.MARGIN);
        setHgrow(buySellForm, Priority.ALWAYS);
        setHgrow(distributionForm, Priority.ALWAYS);
    }
}
