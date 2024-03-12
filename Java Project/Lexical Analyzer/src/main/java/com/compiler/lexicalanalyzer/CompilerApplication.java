package com.compiler.lexicalanalyzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CompilerApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setMinHeight(660);
        stage.setMinWidth(960);
        FXMLLoader fxmlLoader = new FXMLLoader(CompilerApplication.class.getResource("Compiler.fxml"));
        stage.getIcons().add(new Image(Objects.requireNonNull(CompilerApplication.class.getResourceAsStream("Clanguage.png"))));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("C Compiler");
        stage.setScene(scene);
        stage.show();
    }
}