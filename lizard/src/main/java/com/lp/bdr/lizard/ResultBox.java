package com.lp.bdr.lizard;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.Node;

import java.util.List;

public class ResultBox extends VBox {
    private final Label successLabel;
    private final Label errorLabel;
    private final Label outputTitleLabel;
    private final Label outputLabel;

    public ResultBox() {
        setAlignment(Pos.CENTER);

        successLabel = new Label("La requête SQL s'est terminée sans erreur.");
        errorLabel = new Label();

        outputTitleLabel = new Label("Sortie textuelle de la requête :");
        outputLabel = new Label();

        successLabel.setWrapText(true);
        errorLabel.setWrapText(true);
        outputTitleLabel.setWrapText(true);
        outputLabel.setWrapText(true);

        errorLabel.setTextFill(Color.RED);
    }

    public void reset() {
        List<Node> childs = getChildren();

        childs.remove(successLabel);
        childs.remove(errorLabel);
        childs.remove(outputTitleLabel);
        childs.remove(outputLabel);
    }

    public void showSuccess() {
        getChildren().setAll(successLabel);
    }

    public void showError(String errorMessage) {
        errorLabel.setText("La requête SQL s'est terminée avec erreur : "
            + errorMessage);

        getChildren().setAll(errorLabel);
    }

    /* it's like oracle jdbc output arrays always end with a "null" entry, we
       ignore it */
    public void showOutput(String[] output) {
        if(output.length <= 1) return;

        String outputConc = output[0];

        for(int i = 1; i < output.length - 1; i++) {
            outputConc += "\n" + output[i];
        }

        outputLabel.setText(outputConc);
        getChildren().addAll(outputTitleLabel, outputLabel);
    }
}
