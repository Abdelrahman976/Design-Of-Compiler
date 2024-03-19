package com.compiler.lexicalanalyzer;

import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
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
                viewWindow.getStyleClass().removeAll("showTokenTable", "showSymbolTable");
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
    TableView<LexicalAnalyzer.Token> tokensTable;
    @FXML
    void scanCode(MouseEvent event) {
        if (file == null)
            new Alert("Error","Please select a file to scan!", "red");
        else if(vboxView.getChildren().contains(tokensTable)){
            new Alert("Info","Code already scanned!", "blue");
        }else {
            codeArea.setScrollTop(0);
            viewWindow.getStyleClass().removeAll("hide", "showSymbolTable");
            viewWindow.getStyleClass().add("showTokenTable");
            vboxView.getChildren().clear();
            for (String type : tokenType) {
                tokensTable = createTable(type);
                if (tokensTable != null){
                    vboxView.getChildren().add(tokensTable);
                }
            }
        }
    }

    private TableView<LexicalAnalyzer.Token> createTable(String type){
        TableView<LexicalAnalyzer.Token> tokensTable = new TableView<>();
        tokensTable.setMinHeight(300);
        tokensTable.getStyleClass().add("table-view");
        tokensTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        TableColumn<LexicalAnalyzer.Token, String> lexemeCol = new TableColumn<>(type);
        lexemeCol.setCellValueFactory(new PropertyValueFactory<>("Lexeme"));
        colFactory(lexemeCol);
        tokensTable.getItems().clear();
        for (LexicalAnalyzer.Token token : lexicalAnalyzer.getTokens())
            if(token.Tokentype.equals(type))
                tokensTable.getItems().add(token);
        setColFactory(lexemeCol);
        if (!tokensTable.getItems().isEmpty()) {
            tokensTable.getColumns().add(lexemeCol);
            return tokensTable;
        } else
            return null;
    }
    TableView<LexicalAnalyzer.SymbolTable> symbolTable;
    @FXML
    void symbolTable(MouseEvent event) {
        if (file == null)
            new Alert("Error","Please select a file to show its symbol table!", "red");
        else if(vboxView.getChildren().contains(symbolTable)){
            new Alert("Info","Symbol Table already created!", "blue");
        }else {
            codeArea.setScrollTop(0);
            viewWindow.getStyleClass().removeAll("hide", "showTokenTable");
            viewWindow.getStyleClass().add("showSymbolTable");
            symbolTable = createSymbolTable();
            vboxView.getChildren().clear();
            vboxView.getChildren().add(symbolTable);
        }
    }
    private TableView<LexicalAnalyzer.SymbolTable> createSymbolTable(){
        TableView<LexicalAnalyzer.SymbolTable> symbolTable = new TableView<>();
        VBox.setVgrow(symbolTable, Priority.ALWAYS);
        symbolTable.getStyleClass().add("table-view");
        symbolTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        TableColumn<LexicalAnalyzer.SymbolTable, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colFactory(idCol);
        TableColumn<LexicalAnalyzer.SymbolTable, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        colFactory(nameCol);
        TableColumn<LexicalAnalyzer.SymbolTable, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        colFactory(typeCol);
        TableColumn<LexicalAnalyzer.SymbolTable, String> lineOfDecCol = new TableColumn<>("Line of Declaration");
        lineOfDecCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().line_of_references.getFirst().toString())
        );// To display the line of declaration in the table
        colFactory(lineOfDecCol);
        TableColumn<LexicalAnalyzer.SymbolTable, String> lineOfRefCol = new TableColumn<>("Line of Reference");
        lineOfRefCol.setCellValueFactory(cellData -> {
            ArrayList<Integer> lineOfReferences = cellData.getValue().line_of_references;
            if (lineOfReferences.size() > 1) {
                ArrayList<Integer> sublist = new ArrayList<>(lineOfReferences.subList(1, lineOfReferences.size()));
                return new SimpleObjectProperty<>(sublist.toString());
            } else {
                return new SimpleObjectProperty<>("No Reference");
            }
        });// To display the lines of references in the table
        colFactory(lineOfRefCol);
        symbolTable.getItems().clear();
        // Set the cell factory for each column
        setColFactory(idCol,"ID");
        setColFactory(nameCol,"Name");
        setColFactory(typeCol,"Type");
        setColFactory(lineOfDecCol,"Line of Declaration");
        setColFactory(lineOfRefCol,"Line of Reference");

        for (LexicalAnalyzer.SymbolTable row : lexicalAnalyzer.getSymbolTable()){
            symbolTable.getItems().add(row);
        }
        if (!symbolTable.getItems().isEmpty()) {
            symbolTable.getColumns().addAll(idCol,nameCol, typeCol, lineOfDecCol, lineOfRefCol);
            return symbolTable;
        } else {
            return null;
        }
    }
    private void setColFactory(TableColumn<LexicalAnalyzer.SymbolTable, String> col,String colName){
        // Create a cell factory
        Callback<TableColumn<LexicalAnalyzer.SymbolTable, String>, TableCell<LexicalAnalyzer.SymbolTable, String>> cellFactory = new Callback<>() {
            @Override
            public TableCell<LexicalAnalyzer.SymbolTable, String> call(TableColumn<LexicalAnalyzer.SymbolTable, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                            // Calculate the width of the cell's content
                            Text text = new Text();
                            double maxWidth = 0;
                            double width = 0;
                            for (LexicalAnalyzer.SymbolTable col : symbolTable.getItems()) {
                                switch (colName) {
                                    case "ID" -> {text.setText(col.getID());
                                                  width = text.getLayoutBounds().getWidth();}
                                    case "Name" -> {text.setText(col.getName());
                                                    width = text.getLayoutBounds().getWidth();}
                                    case "Type" -> {text.setText(col.getType());
                                                    width = text.getLayoutBounds().getWidth();}
                                    case "Line of Declaration" -> {text.setText(col.getLine_of_references().getFirst().toString());
                                                                   width = text.getLayoutBounds().getWidth();}
                                    case "Line of Reference" -> {
                                        if (col.getLine_of_references().size() > 1) {
                                            ArrayList<Integer> sublist = new ArrayList<>(col.getLine_of_references().subList(1, col.getLine_of_references().size()));
                                            text.setText(sublist.toString());
                                        } else
                                            text.setText("No Reference");
                                        width = text.getLayoutBounds().getWidth();
                                    }
                                }
                                if (width > maxWidth) {
                                    maxWidth = width;
                                }

                            }
                            double headerWidth = new Text(colName).getLayoutBounds().getWidth();
                            if (Objects.equals(colName, "ID")){
                                param.setMinWidth(maxWidth * 2.5);
                                param.setMaxWidth(maxWidth * 2.5);
                            } else if (maxWidth < headerWidth) {
                                param.setMinWidth(headerWidth * 1.7);
                                param.setMaxWidth(headerWidth * 1.7);
                            } else {
                                param.setMinWidth(maxWidth * 1.7); // Add some padding
                                param.setMaxWidth(maxWidth * 1.7); // Add some padding
                            }
                        }
                    }
                };
            }
        };
        col.setCellFactory(cellFactory);
    }
    private void setColFactory(TableColumn<LexicalAnalyzer.Token,String> col){
        Callback<TableColumn<LexicalAnalyzer.Token, String>, TableCell<LexicalAnalyzer.Token, String>> cellFactory = new Callback<>() {
            @Override
            public TableCell<LexicalAnalyzer.Token, String> call(TableColumn<LexicalAnalyzer.Token, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                            // Calculate the width of the cell's content
                            Text text = new Text();
                            double maxWidth = 0;
//                            double width = 0;
                            for (LexicalAnalyzer.Token tokenType : lexicalAnalyzer.getTokens()) {
                                text.setText(tokenType.Lexeme);
                                System.out.println(tokenType.Lexeme+" "+text.getLayoutBounds().getWidth()   );
                                double width = text.getLayoutBounds().getWidth();
                                if (width > maxWidth) {
                                    maxWidth = width;
                                    System.out.println("|                         "+maxWidth+" "+width+"                                 |");
                                }
                            }
                            for (String type : tokenType) {
                                double headerWidth = new Text(type).getLayoutBounds().getWidth();
                                if (maxWidth < headerWidth) {
                                    param.setMinWidth(headerWidth);
                                    param.setMaxWidth(headerWidth);
                                } else {
                                    param.setMinWidth(maxWidth); // Add some padding
                                    param.setMaxWidth(maxWidth); // Add some padding
                                }
                            }
                        }
                    }
                };
            }
        };
        col.setCellFactory(cellFactory);
    }
    private void colFactory(TableColumn<?,?> col){
        col.setResizable(false);
        col.setEditable(false);
        col.setReorderable(false);
        col.setSortable(false);
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