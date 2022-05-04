package com.lp.bdr.lizard;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
    public static final int MARGIN = 16;

    @Override
    public void start(Stage stage) {
        stage.setTitle("lizard");
        stage.setWidth(800);
        stage.setHeight(800);

        MainBox mainBox = new MainBox();

        Scene scene = new Scene(mainBox);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
