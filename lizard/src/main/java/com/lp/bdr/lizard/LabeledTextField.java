package com.lp.bdr.lizard;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LabeledTextField extends VBox {
    private final TextField textField;

    protected TextField createTextField() {
        return new TextField();
    }

    public LabeledTextField(String labelText) {
        Label label = new Label(labelText);
        textField = createTextField();

        getChildren().addAll(label, textField);
    }

    public String getValue() {
        return textField.getText();
    }
}
