package com.lp.bdr.lizard;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.geometry.Pos;

import java.util.ArrayList;

public class Form extends VBox {
    private ArrayList<Integer> ids;
    private VBox fields;
    private Button validateButton;

    public Form(String validateButtonText) {
        ids = new ArrayList<Integer>();
        fields = new VBox();
        fields.setSpacing(Main.MARGIN);

        validateButton = new Button(validateButtonText);
        HBox validateButtonWrapper = new HBox(validateButton);
        validateButtonWrapper.setAlignment(Pos.BASELINE_RIGHT);

        getChildren().addAll(fields, validateButtonWrapper);
        setSpacing(Main.MARGIN);
    }

    public void addField(int id, String fieldLabelText) {
        ids.add(id);
        LabeledTextField field = new LabeledTextField(fieldLabelText);
        fields.getChildren().add(field);
    }

    private String getFieldValueByIndex(int fieldIndex) {
        Node fieldNode = fields.getChildren().get(fieldIndex);
        LabeledTextField field = (LabeledTextField)fieldNode;
        return field.getValue();
    }

    public String getFieldValue(int fieldId) {
        for(int i = 0; i < ids.size(); i++) {
            if(ids.get(i) == fieldId) {
                return getFieldValueByIndex(i);
            }
        }

        return null;
    }

    public void setValidateButtonText(String validateButtonText) {
        validateButton.setText(validateButtonText);
    }
}
