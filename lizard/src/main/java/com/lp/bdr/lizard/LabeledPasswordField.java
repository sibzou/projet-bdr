package com.lp.bdr.lizard;

import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class LabeledPasswordField extends LabeledTextField {
    public LabeledPasswordField(String labelText) {
        super(labelText);
    }

    @Override
    protected TextField createTextField() {
        return new PasswordField();
    }
}
