package com.lp.bdr.lizard;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LabeledTextField extends VBox {
    private final TextField textField;
    private final Label errorLabel;

    protected TextField createTextField() {
        return new TextField();
    }

    public LabeledTextField(String labelText) {
        Label label = new Label(labelText);
        textField = createTextField();

        errorLabel = new Label("Ce champ est incorrect.");
        errorLabel.setTextFill(Color.RED);

        getChildren().addAll(label, textField);
    }

    public String getValue() {
        return textField.getText();
    }

    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
        getChildren().add(errorLabel);
    }

    public void hideError() {
        getChildren().remove(errorLabel);
    }

    public int safeParseInt(ErrorReporter errorReporter) {
        try {
            return Integer.parseInt(getValue());
        } catch(NumberFormatException exception) {
            showError("Un nombre entier est attendu.");
            errorReporter.error = true;
            return 0;
        }
    }

    public float safeParseFloat(ErrorReporter errorReporter) {
        try {
            return Float.parseFloat(getValue());
        } catch(NumberFormatException exception) {
            showError("Un nombre est attendu.");
            errorReporter.error = true;
            return 0;
        }
    }
}
