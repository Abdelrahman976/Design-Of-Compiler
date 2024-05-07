package com.compiler.lexicalanalyzer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompilerController implements Initializable {

    FileChooser fileChooser = new FileChooser();
    LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
    public static File file;
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
    private Stage parserStage;

    @FXML
    void readCode(MouseEvent event) {
        file = fileChooser.showOpenDialog(new Stage());
        if (file != null){
            previousFile = file;
        }else if(previousFile == null){
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
    String [] tokenType = {"Keyword", "Identifier", "Operator", "Integer", "Float", "Long", "Long Long", "String Literal", "Char Literal", "Punctuation","Error"};
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
        Stage stage = (Stage) vboxView.getScene().getWindow();
        tokensTable.minHeightProperty().bind(stage.heightProperty().divide(2));
        tokensTable.getStyleClass().add("table-view");
        tokensTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tokensTable.setClip(roundedListview(tokensTable));
        TableColumn<LexicalAnalyzer.Token, String> lexemeCol = new TableColumn<>(type);
        lexemeCol.setCellValueFactory(new PropertyValueFactory<>("Lexeme"));
        colFactory(lexemeCol,true);
        tokensTable.getItems().clear();
        for (LexicalAnalyzer.Token token : lexicalAnalyzer.getTokens()){
            if(token.Tokentype.equals(type)){
                tokensTable.getItems().add(token);
            }
        }
        if(type.equals("Operator")){
            for (LexicalAnalyzer.Token token1 : lexicalAnalyzer.getTokens()){
                if(token1.Tokentype.equals("RelOp")){
                    tokensTable.getItems().add(token1);
                }
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
    TableView<LexicalAnalyzer.SymbolTable> symbolTable;
    String selectedSymbol = null;
    int lastIndex = 0;
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
            symbolTable.setOnMouseClicked(e-> {
                LexicalAnalyzer.SymbolTable selectedItem = symbolTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    String newSelection = selectedItem.getName();
                    if (selectedSymbol == null || !selectedSymbol.equals(newSelection)) {
                        // If the selected symbol has changed, start searching from the beginning
                        selectedSymbol = newSelection;
                        lastIndex = 0;
                    }
                    // Find the next occurrence of the selected symbol
                    Pattern pattern = Pattern.compile("\\b" + selectedSymbol + "\\b");
                    Matcher matcher = pattern.matcher(codeArea.getText().substring(lastIndex));
                    if (matcher.find()) {
                        // If the selected symbol is found, highlight it and update lastIndex
                        int index = lastIndex + matcher.start();
                        codeArea.selectRange(index, index + selectedSymbol.length());
                        lastIndex = index + selectedSymbol.length();
                    } else {
                        lastIndex = 0;
                    }
                }
            });
        }
    }
    private TableView<LexicalAnalyzer.SymbolTable> createSymbolTable(){
        TableView<LexicalAnalyzer.SymbolTable> symbolTable = new TableView<>();
        symbolTable.setClip(roundedListview(symbolTable));
        VBox.setVgrow(symbolTable, Priority.ALWAYS);
        symbolTable.getStyleClass().add("table-view");
        symbolTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        TableColumn<LexicalAnalyzer.SymbolTable, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colFactory(idCol,false);
        TableColumn<LexicalAnalyzer.SymbolTable, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        colFactory(nameCol,false);
        TableColumn<LexicalAnalyzer.SymbolTable, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        colFactory(typeCol,false);
        TableColumn<LexicalAnalyzer.SymbolTable, String> lineOfDecCol = new TableColumn<>("Line of Declaration");
        lineOfDecCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().line_of_references.getFirst().toString())
        );// To display the line of declaration in the table
        colFactory(lineOfDecCol,false);
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
        colFactory(lineOfRefCol,false);
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
                                param.setMinWidth(headerWidth * 1.9);
                                param.setMaxWidth(headerWidth * 1.9);
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

    private void colFactory(TableColumn<?,?> col,boolean isResizable){
        col.setResizable(isResizable);
        col.setEditable(false);
        col.setReorderable(false);
        col.setSortable(false);
    }
    @FXML
    void parseCode(MouseEvent event) {
        if (file == null)
            new Alert("Error","Please select a file to parse!", "red");
        else if (parserStage != null && parserStage.isShowing()){
            new Alert("Info","Parse Table Window Already Open!", "blue");
        }else {
            try {
                parserStage = new Stage();
                parserStage.setMinHeight(660);
                parserStage.setMinWidth(960);
                FXMLLoader fxmlLoader = new FXMLLoader(CompilerApplication.class.getResource("Parser.fxml"));
                parserStage.getIcons().add(new Image(Objects.requireNonNull(CompilerApplication.class.getResourceAsStream("Clanguage.png"))));
                Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
                parserStage.setTitle("C Parser");
                parserStage.setScene(scene);
                parserStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Rectangle roundedListview(TableView table){
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(table.widthProperty());
        clip.heightProperty().bind(table.heightProperty());
        clip.setArcHeight(20);
        clip.setArcWidth(20);
        return clip;
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