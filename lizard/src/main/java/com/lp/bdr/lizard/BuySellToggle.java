package com.lp.bdr.lizard;

import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Toggle;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.beans.value.ObservableValue;

public class BuySellToggle extends HBox {
    private ToggleButton buyButton;
    private ToggleButton sellButton;
    private BuySellSwitchHandler swtHandler;

    public BuySellToggle(BuySellSwitchHandler swtHandler) {
        buyButton = new ToggleButton("Acheter");
        sellButton = new ToggleButton("Vendre");
        this.swtHandler = swtHandler;

        ToggleGroup buySellToggleGroup = new ToggleGroup();

        buyButton.setToggleGroup(buySellToggleGroup);
        sellButton.setToggleGroup(buySellToggleGroup);

        buySellToggleGroup.selectedToggleProperty().addListener(
            this::onSelectedToggleChange);

        buyButton.setSelected(true);

        getChildren().addAll(buyButton, sellButton);
        setAlignment(Pos.BASELINE_CENTER);
        setSpacing(Main.MARGIN / 2);
    }

    private void onSelectedToggleChange(
            ObservableValue<? extends Toggle> observable, Toggle oldValue,
            Toggle newValue) {

        if(newValue == null) {
            oldValue.setSelected(true);
        } else if(newValue == buyButton) {
            swtHandler.onBuyMode();
        } else if(newValue == sellButton) {
            swtHandler.onSellMode();
        }
    }
}
