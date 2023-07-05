package com.example.projekt_60134_io;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("first-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 470, 360);
        stage.setResizable(false);
        stage.setTitle("Witaj!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}