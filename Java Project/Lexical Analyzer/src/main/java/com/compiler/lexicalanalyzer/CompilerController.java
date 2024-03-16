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
        if (file != null){
            previousFile = file;}
        else if(previousFile == null){
            codeArea.setScrollTop(0);
            return;
        }
        else{
            file = previousFile;
            codeArea.setScrollTop(0);
            return;
        }
        try {
            Scanner scanner = new Scanner(file);
            if (!codeArea.getText().isEmpty()) {
                lineArea.clear();
                codeArea.clear();
                viewWindow.getStyleClass().add("hide");
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
            lexicalAnalyzer = lexicalAnalyzer.start(new LexicalAnalyzer(),file.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    String [] tokenType = {"Keyword", "Identifier", "Operator", "Integer", "Float", "Long", "Long Long", "String Literal", "Char Literal", "Punctuation"};
    @FXML
    void scanCode(MouseEvent event) {
        if (file == null)
            new Alert("Error","Please select a file to scan!", "red");
        else if(!vboxView.getChildren().isEmpty()){
            new Alert("Info","Code already scanned!", "blue");
        }else {
            codeArea.setScrollTop(0);
            viewWindow.getStyleClass().removeAll("hide");
            viewWindow.getStyleClass().add("showView");
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
        tokensTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
                    lexemeCol.setMaxWidth(Control.USE_PREF_SIZE);
                    return;
                }
            }
        });
        if (!tokensTable.getItems().isEmpty()) {
            tokensTable.getColumns().add(lexemeCol);
            return tokensTable;
        } else {
            return null;
        }
    }

    @FXML
    void symbolTable(MouseEvent event) {
        if (file == null)
            new Alert("Error","Please select a file to show its symbol table!", "red");
        else if(!vboxView.getChildren().isEmpty()){
            new Alert("Info","Symbol Table already created!", "blue");
        }else {
            codeArea.setScrollTop(0);
            viewWindow.getStyleClass().removeAll("hide");
            viewWindow.getStyleClass().add("showSymbolTable");
            TableView<LexicalAnalyzer.SymbolTable> table = createSymbolTable();
            vboxView.getChildren().add(table);
        }
    }

    private TableView<LexicalAnalyzer.SymbolTable> createSymbolTable(){
        TableView<LexicalAnalyzer.SymbolTable> symbolTable = new TableView<>();
//        symbolTable.setMinHeight(250);
        symbolTable.setMinHeight(700);
        symbolTable.setMinWidth(700);
        symbolTable.getStyleClass().add("table-view");
        symbolTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<LexicalAnalyzer.SymbolTable, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idCol.setResizable(true);
        idCol.setEditable(false);
        TableColumn<LexicalAnalyzer.SymbolTable, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setResizable(true);
        nameCol.setEditable(false);
        TableColumn<LexicalAnalyzer.SymbolTable, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setResizable(true);
        typeCol.setEditable(false);
        TableColumn<LexicalAnalyzer.SymbolTable, String> lineOfDecCol = new TableColumn<>("Line of Declaration");
        lineOfDecCol.setCellValueFactory(new PropertyValueFactory<>("line_of_references"));
        lineOfDecCol.setResizable(true);
        lineOfDecCol.setEditable(false);
        TableColumn<LexicalAnalyzer.SymbolTable, String> lineOfRefCol = new TableColumn<>("Line of Reference");
        lineOfRefCol.setCellValueFactory(new PropertyValueFactory<>("line_of_references"));
        lineOfRefCol.setResizable(true);
        lineOfRefCol.setEditable(false);
        symbolTable.getItems().clear();
        for (LexicalAnalyzer.SymbolTable row : lexicalAnalyzer.getSymbolTable()){
            System.out.println(row.line_of_references);

            symbolTable.getItems().add(row);
        }
        if (!symbolTable.getItems().isEmpty()) {
//            symbolTable.getColumns().add(idCol);
//            symbolTable.getColumns().add(nameCol);
//            symbolTable.getColumns().add(typeCol);
//            symbolTable.getColumns().add(lineOfDecCol);
//            symbolTable.getColumns().add(lineOfRefCol);
            symbolTable.getColumns().addAll(idCol,nameCol, typeCol, lineOfDecCol, lineOfRefCol);
            return symbolTable;
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
        viewWindow.getStyleClass().add("hide");
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