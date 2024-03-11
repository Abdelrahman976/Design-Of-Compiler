package com.compiler.lexicalanalyzer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.List;

public class CompilerController implements Initializable {

    FileChooser fileChooser = new FileChooser();
    LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
    File file;
    @FXML
    private TextArea codeArea;
    @FXML
    private TextArea lineArea;
    @FXML
    private VBox vboxView;
    @FXML
    private AnchorPane viewWindow;

    @FXML
    void readCode(MouseEvent event) {
        file = fileChooser.showOpenDialog(new Stage());
        if (file == null) {
            return;
        }
        try {
            Scanner scanner = new Scanner(file);
            if (!codeArea.equals("")){
                lineArea.clear();
                codeArea.clear();
                viewWindow.getStyleClass().add("hideView");
                viewWindow.getStyleClass().removeAll("showView");
                vboxView.getChildren().clear();
            }
            int i = 0;
            while (scanner.hasNextLine()) {
                i++;
                lineArea.appendText(i+"\n");
                codeArea.appendText(scanner.nextLine()+"\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }
String [] tokenType = {"Keyword", "Identifier", "Operator", "Integer", "Float", "String", "Char", "Punctuation"};
    @FXML
    void scanCode(MouseEvent event) {
        if (file == null)
            new Alert("Error","Please choose a code to scan!", "red");
        else if(!vboxView.getChildren().isEmpty()){
            new Alert("Info","Code already scanned!", "blue");
        }else {
            viewWindow.getStyleClass().removeAll("hideView");
            viewWindow.getStyleClass().add("showView");
            lexicalAnalyzer = lexicalAnalyzer.start(new LexicalAnalyzer(),file.getAbsolutePath());
            for (String type : tokenType) {
                TableView<LexicalAnalyzer.Token> table = createTable(type);
                if (table != null){
                    Label label = new Label(type);
                    label.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
                    vboxView.getChildren().add(label);
                    vboxView.getChildren().add(table);
                }
            }
        }
    }
    private TableView<LexicalAnalyzer.Token> createTable(String type){
        TableView<LexicalAnalyzer.Token> tokensTable = new TableView<>();
        tokensTable.setMinHeight(200);
        tokensTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<LexicalAnalyzer.Token, String> lexemeCol = new TableColumn<>("Lexemes");
        lexemeCol.setCellValueFactory(new PropertyValueFactory<>("Lexeme"));
        lexemeCol.setResizable(true);
        lexemeCol.setEditable(false);
        tokensTable.getItems().clear();
        for (LexicalAnalyzer.Token token : lexicalAnalyzer.getTokens()){
            if(token.Tokentype.equals(type)){
                tokensTable.getItems().add(token);
            }
        }
        if (!tokensTable.getItems().isEmpty()) {
            tokensTable.getColumns().add(lexemeCol);
            return tokensTable;
        } else {
            return null;
        }
    }
    @FXML
    void parseCode(MouseEvent event) {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser.setInitialDirectory(new File("../../"));
        codeArea.scrollTopProperty().addListener((observable, oldValue, newValue) -> {
            lineArea.setScrollTop(newValue.doubleValue());
        });
        viewWindow.getStyleClass().add("hideView");
    }
}