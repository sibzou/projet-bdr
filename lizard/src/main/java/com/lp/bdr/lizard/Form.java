package com.lp.bdr.lizard;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.geometry.Pos;

import java.util.List;
import java.util.ArrayList;

public class Form extends VBox {
    private ArrayList<Integer> ids;
    private VBox fields;
    private Button validateButton;
    private FormValidationHandler validationHandler;

    public Form(String validateButtonText) {
        ids = new ArrayList<Integer>();
        fields = new VBox();
        fields.setSpacing(Main.MARGIN);

        validateButton = new Button(validateButtonText);
        HBox validateButtonWrapper = new HBox(validateButton);

        validateButtonWrapper.setAlignment(Pos.BASELINE_RIGHT);
        validateButton.setOnAction(event -> validationHandler.onValidate());

        getChildren().addAll(fields, validateButtonWrapper);
        setSpacing(Main.MARGIN);
    }

    private void addFieldObj(int id, LabeledTextField field) {
        ids.add(id);
        fields.getChildren().add(field);
    }

    public void addField(int id, String fieldLabelText) {
        addFieldObj(id, new LabeledTextField(fieldLabelText));
    }

    public void addPasswordField(int id, String fieldLabelText) {
        addFieldObj(id, new LabeledPasswordField(fieldLabelText));
    }

    private LabeledTextField getField(int fieldId) {
        for(int i = 0; i < ids.size(); i++) {
            if(ids.get(i) == fieldId) {
                return (LabeledTextField)fields.getChildren().get(i);
            }
        }

        return null;
    }

    public String getFieldValue(int fieldId) {
        return getField(fieldId).getValue();
    }

    public void setValidateButtonText(String validateButtonText) {
        validateButton.setText(validateButtonText);
    }

    public void setOnValidate(FormValidationHandler validationHandler) {
        this.validationHandler = validationHandler;
    }

    public void showError(int fieldId, String errorMessage) {
        getField(fieldId).showError(errorMessage);
    }

    public void hideErrors() {
        List<Node> childs = fields.getChildren();

        for(Node node : childs) {
            LabeledTextField field = (LabeledTextField)node;
            field.hideError();
        }
    }

    public int safeParseQueryInt(int fieldId, ErrorReporter errorReporter) {
        return getField(fieldId).safeParseInt(errorReporter);
    }

    public float safeParseQueryFloat(int fieldId, ErrorReporter errorReporter) {
        return getField(fieldId).safeParseFloat(errorReporter);
    }
}
