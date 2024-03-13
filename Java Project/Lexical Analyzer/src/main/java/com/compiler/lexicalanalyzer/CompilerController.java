package com.compiler.lexicalanalyzer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
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
import java.util.ResourceBundle;
import java.util.Scanner;

public class CompilerController implements Initializable {

    FileChooser fileChooser = new FileChooser();
    LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
    File file;
    File previousFile = null;
    @FXML
    private TextArea codeArea;
    @FXML
    private TextArea lineArea;
    @FXML
    private VBox vboxView;
    @FXML
    private AnchorPane viewWindow;
    @FXML
    private VBox infoBox;


    @FXML
    void readCode(MouseEvent event) {
        file = fileChooser.showOpenDialog(new Stage());
        if (file != null)
            previousFile = file;
        else if(previousFile == null)
            return;
        else{
            file = previousFile;
            return;
        }
        try {
            Scanner scanner = new Scanner(file);
            if (!codeArea.equals("")) {
                lineArea.clear();
                codeArea.clear();
                viewWindow.getStyleClass().add("hideView");
                viewWindow.getStyleClass().removeAll("showView");
                vboxView.getChildren().clear();
            }
            int i = 0;
            infoBox.setVisible(false);
            while (scanner.hasNextLine()) {
                i++;
                lineArea.appendText(i + "\n");
                codeArea.appendText(scanner.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    String [] tokenType = {"Keyword", "Identifier", "Operator", "Integer", "Float", "Long", "Long Long", "String Literal", "Char Literal", "Punctuation", "BadString"};
    @FXML
    void scanCode(MouseEvent event) {
        if (file == null)
            new Alert("Error","Please select a file to scan!", "red");
        else if(!vboxView.getChildren().isEmpty()){
            new Alert("Info","Code already scanned!", "blue");
        }else {
            viewWindow.getStyleClass().removeAll("hideView");
            viewWindow.getStyleClass().add("showView");
            lexicalAnalyzer = lexicalAnalyzer.start(new LexicalAnalyzer(),file.getAbsolutePath());
            for (String type : tokenType) {
                TableView<LexicalAnalyzer.Token> table = createTable(type);
                if (table != null){
                    vboxView.getChildren().add(table);
                }
            }
        }
    }

    private TableView<LexicalAnalyzer.Token> createTable(String type){
        TableView<LexicalAnalyzer.Token> tokensTable = new TableView<>();
        tokensTable.setMinHeight(250);
        tokensTable.getStyleClass().add("table-view");
        tokensTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        TableColumn<LexicalAnalyzer.Token, String> lexemeCol = new TableColumn<>(type);
        lexemeCol.setCellValueFactory(new PropertyValueFactory<>("Lexeme"));
        lexemeCol.setResizable(true);
        lexemeCol.setEditable(false);
        tokensTable.getItems().clear();
        for (LexicalAnalyzer.Token token : lexicalAnalyzer.getTokens()){
            if(token.Tokentype.equals(type)){
                tokensTable.getItems().add(token);
            }
        }
        // Check if the content of the cells in the column exceeds the width of the column
        lexemeCol.widthProperty().addListener((obs, oldVal, newVal) -> {
            for (LexicalAnalyzer.Token item : tokensTable.getItems()) {
                if (lexemeCol.getCellData(item) != null && lexemeCol.getCellData(item).length() > newVal.doubleValue()) {
                    lexemeCol.setMinWidth(Control.USE_PREF_SIZE);
                    return;
                }
            }
            lexemeCol.setMinWidth(349);
        });
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
        fileChooser.setInitialDirectory(new File("../../TestCases"));
        codeArea.scrollTopProperty().addListener((observable, oldValue, newValue) -> lineArea.setScrollTop(newValue.doubleValue()));
        viewWindow.getStyleClass().add("hideView");
        codeArea.getStyleClass().add("text-area");
        codeArea.layoutBoundsProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                Node content = codeArea.lookup(".content");
                if (content != null && !codeArea.getText().isEmpty()) {
                    double contentWidth = content.getBoundsInLocal().getWidth();
                    double textAreaWidth = newValue.getWidth();
                    if (contentWidth > textAreaWidth - 6) {
                        lineArea.setPadding(new Insets(0, 0, 13, 0));
                    } else {
                        lineArea.setPadding(new Insets(0, 0, 0, 0));
                    }
                }
            }
        });
    }
}