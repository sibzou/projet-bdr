package com.lp.bdr.lizard;

import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MainBox extends VBox {
    public MainBox() {
        LabeledTextField accountTextField
            = new LabeledTextField("Numéro de compte");
        accountTextField.setMaxWidth(400);

        UseCasesBox useCasesBox = new UseCasesBox();
        ResultBox resultBox = new ResultBox();
        getChildren().addAll(accountTextField, useCasesBox, resultBox);

        setPadding(new Insets(Main.MARGIN));
        setAlignment(Pos.TOP_CENTER);

        setMargin(accountTextField, new Insets(0, 0, 2 * Main.MARGIN, 0));
        setMargin(useCasesBox, new Insets(0, 0, 4 * Main.MARGIN, 0));
    }
}
