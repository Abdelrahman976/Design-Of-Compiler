package com.compiler.lexicalanalyzer;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class Alert {

    public Alert(){}
    public Alert(String title, String message, String color){
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);

        alertStage.setTitle(title);
        alertStage.getIcons().add(new Image(Objects.requireNonNull(Alert.class.getResourceAsStream("Clanguage.png"))));

        Label msg = new Label();
        msg.setText(message);
        msg.setWrapText(true);
        msg.setAlignment(Pos.CENTER);
        msg.setTextAlignment(TextAlignment.CENTER);
        msg.setPadding(new Insets(0,20,5,20));
        msg.setStyle("-fx-font-weight:bold;-fx-text-fill:"+ color);

        JFXButton ok = new JFXButton("OK");
        ok.setFocusTraversable(false);
        ok.setStyle("-fx-background-radius:15;-fx-focus-color:transparent;-fx-faint-focus-color:transparent;-fx-cursor:hand;-fx-border-color: black;-fx-border-radius: 15");
        ok.setPadding(new Insets(10,20,10,20));
        ok.setOnAction(e -> alertStage.close());

        VBox vBox= new VBox();
        vBox.getChildren().addAll(msg,ok);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Scene alert = new Scene(vBox,250,120);

        alertStage.setResizable(false);
        alertStage.setScene(alert);
        alertStage.showAndWait();
    }
    boolean choice;
    public boolean Alert(String title,String message,String color,String btnLabel1,String btnLabel2){
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.setTitle(title);
        alertStage.getIcons().add(new Image(Alert.class.getResourceAsStream("Clanguage.png")));

        Label msg = new Label();
        msg.setText(message);
        msg.setWrapText(true);
        msg.setStyle("-fx-font-weight:bold;-fx-text-fill:"+ color );
        msg.setAlignment(Pos.CENTER);
        msg.setTextAlignment(TextAlignment.CENTER);
        msg.setPadding(new Insets(0,20,5,20));

        JFXButton btn1 = new JFXButton(btnLabel1);
        btn1.setStyle("-fx-background-color:lightgrey;-fx-background-radius:15;-fx-focus-color:transparent;-fx-faint-focus-color:transparent;-fx-cursor:hand;-fx-border-color: black;-fx-border-radius: 15");
        btn1.setPadding(new Insets(10,20,10,20));
        btn1.setFocusTraversable(false);;
        btn1.setOnAction(e -> {
            choice = true;
            alertStage.close();
        });

        JFXButton btn2 = new JFXButton(btnLabel2);
        btn2.setStyle("-fx-background-color:lightgrey;-fx-background-radius:15;-fx-focus-color:transparent;-fx-faint-focus-color:transparent;-fx-cursor:hand;-fx-border-color: black;-fx-border-radius: 15");
        btn2.setPadding(new Insets(10,20,10,20));
        btn2.setFocusTraversable(false);
        btn2.setOnAction(e -> {
            choice = false;
            alertStage.close();
        });

        VBox vBox= new VBox();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(btn1,btn2);
        vBox.getChildren().addAll(msg,hBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Scene alert = new Scene(vBox,250,130);

        alertStage.setResizable(false);
        alertStage.setScene(alert);
        alertStage.showAndWait();
        return choice;
    }
}
