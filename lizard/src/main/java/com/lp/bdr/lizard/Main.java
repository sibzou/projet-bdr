package com.lp.bdr.lizard;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class Main extends Application {
    public static final int MARGIN = 16;

    private static final int FIELD_VALUE_CODE = 0,
                             FIELD_DATE = 1,
                             FIELD_QUANTITY = 2,
                             FIELD_AMOUNT = 3;

    private static final int FIELD_CRITERIA = 0;

    @Override
    public void start(Stage stage) {
        stage.setTitle("lizard");
        stage.setWidth(800);
        stage.setHeight(600);

        LabeledTextField accountTextField
            = new LabeledTextField("Numéro de compte");
        accountTextField.setMaxWidth(400);

        Form buySellForm = new Form("Acheter ou vendre");
        buySellForm.addField(FIELD_VALUE_CODE, "Code valeur");
        buySellForm.addField(FIELD_DATE, "Date");
        buySellForm.addField(FIELD_QUANTITY, "Quantité");
        buySellForm.addField(FIELD_AMOUNT, "Montant");

        ToggleButton buyButton = new ToggleButton("Acheter");
        ToggleButton sellButton = new ToggleButton("Vendre");
        ToggleGroup buySellToggleGroup = new ToggleGroup();

        buyButton.setToggleGroup(buySellToggleGroup);
        sellButton.setToggleGroup(buySellToggleGroup);

        buySellToggleGroup.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> {

            if(newValue == null) {
                oldValue.setSelected(true);
            } else if(newValue == buyButton) {
                buySellForm.setValidateButtonText("Acheter");
            } else if(newValue == sellButton) {
                buySellForm.setValidateButtonText("Vendre");
            }
        });

        buyButton.setSelected(true);

        HBox buySellToggleHBox = new HBox(buyButton, sellButton);
        buySellToggleHBox.setAlignment(Pos.BASELINE_CENTER);
        buySellToggleHBox.setSpacing(MARGIN / 2);
        buySellForm.getChildren().add(0, buySellToggleHBox);

        Form distributionForm = new Form("Calculer");
        distributionForm.addField(FIELD_CRITERIA, "Critère");

        HBox useCasesHBox = new HBox(buySellForm, distributionForm);
        useCasesHBox.setMaxWidth(800);
        useCasesHBox.setSpacing(MARGIN);
        useCasesHBox.setHgrow(buySellForm, Priority.ALWAYS);
        useCasesHBox.setHgrow(distributionForm, Priority.ALWAYS);

        VBox mainVBox = new VBox(accountTextField, useCasesHBox);
        mainVBox.setPadding(new Insets(MARGIN));
        mainVBox.setSpacing(MARGIN);
        mainVBox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(mainVBox);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
