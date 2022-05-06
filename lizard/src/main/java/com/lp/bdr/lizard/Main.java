package com.lp.bdr.lizard;

import com.lp.bdr.lizard.db.Database;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;

public class Main extends Application {
    public static final int MARGIN = 16;

    @Override
    public void start(Stage stage) {
        stage.setTitle("lizard");
        stage.setWidth(800);
        stage.setHeight(1000);

        Database database = new Database();

        MainBox mainBox = new MainBox(database);
        ScrollPane mainBoxScroll = new ScrollPane(mainBox);
        mainBoxScroll.setFitToWidth(true);

        Scene scene = new Scene(mainBoxScroll);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
