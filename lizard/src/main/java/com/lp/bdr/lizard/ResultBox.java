package com.lp.bdr.lizard;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

public class ResultBox extends VBox {
    private Label errorLabel;
    private Label buySuccessLabel;
    private Label sellSuccessLabel;

    public ResultBox() {
        setAlignment(Pos.CENTER);

        errorLabel = new Label("Une erreur s'est produite.");
        buySuccessLabel = new Label("L'achat a bien été enregistré !");
        sellSuccessLabel = new Label("La vente a bien été enregistrée !");

        errorLabel.setTextFill(Color.RED);
    }

    public void showError() {
        getChildren().setAll(errorLabel);
    }

    public void showBuySuccess() {
        getChildren().setAll(buySuccessLabel);
    }

    public void showSellSuccess() {
        getChildren().setAll(sellSuccessLabel);
    }
}
