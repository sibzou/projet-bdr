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
        getChildren().addAll(accountTextField, useCasesBox);

        setPadding(new Insets(Main.MARGIN));
        setSpacing(Main.MARGIN);
        setAlignment(Pos.TOP_CENTER);
    }
}
