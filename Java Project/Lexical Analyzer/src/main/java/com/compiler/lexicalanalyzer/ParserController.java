package com.compiler.lexicalanalyzer;

import com.compiler.lexicalanalyzer.Parser.AnalysisTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.TextInBox;
import org.abego.treelayout.demo.TextInBoxNodeExtentProvider;
import org.abego.treelayout.demo.swing.TextInBoxTreePane;
import org.abego.treelayout.util.DefaultConfiguration;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.*;
public class ParserController implements Initializable  {
    @FXML
    private TableView<String[]> parseTable;
    JFrame frame= new JFrame();
    AnalysisTable analysisTable = new AnalysisTable(new File("src/main/java/com/compiler/lexicalanalyzer/assets/grammar.txt"),CompilerController.file);

    @FXML
    void showParseTree(MouseEvent event){
        if(frame.isVisible()){
            new Alert("Info","Parse Tree Window Already Opened!", "blue");
        } else {
            try {
                // Assuming analysisTable is your AnalysisTable object
                Deque<String[]> tableData = analysisTable.getTable();
                // Create a TreeLayout object
                TreeLayout<TextInBox> treeLayout = new TreeLayout<>(analysisTable.tree, new TextInBoxNodeExtentProvider(), new DefaultConfiguration<>(60, 40));

                // Create a TextInBoxTreePane object and set the box visible
                TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);
                panel.setBoxVisible(true);

                Container contentPane = frame.getContentPane();
                ((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(
                        10, 10, 10, 10));
                JScrollPane scrollPane = new JScrollPane(panel);
                scrollPane.setPreferredSize(new Dimension(1280, 720));
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
                contentPane.add(scrollPane);
                frame.setTitle("Parse Tree");
                frame.setIconImage(Toolkit.getDefaultToolkit().getImage("src/main/resources/com/compiler/lexicalanalyzer/Clanguage.png"));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Rectangle roundedListview(){
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(parseTable.widthProperty());
        clip.heightProperty().bind(parseTable.heightProperty());
        clip.setArcHeight(20);
        clip.setArcWidth(20);
        return clip;
    }
    private void setHeaderStyle(AnalysisTable analysisTable, TableColumn<String[], String> column,int col){
        Text text = new Text();

        double maxWidth = 0;
        for (String[] row : analysisTable.getTable()) {
            double textWidth = new javafx.scene.text.Text(row[col]).getLayoutBounds().getWidth();
            maxWidth = Math.max(maxWidth, textWidth);
        }

        if(col == 0 )
            column.setMinWidth(2 * maxWidth);
        else if (col == 1) {
            column.setMinWidth(1.5 * maxWidth);
        } else
            column.setPrefWidth(maxWidth + 100);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Clear the existing columns
        parseTable.getColumns().clear();

        // Create columns for the table
        TableColumn<String[], String> stackColumn = new TableColumn<>("Stack");
        stackColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        stackColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER_RIGHT); // Aligns the text to the right
                }
            }
        });

        // Add the CSS class to the header
        stackColumn.getStyleClass().addAll("stack-col","label");


        TableColumn<String[], String> tokensColumn = new TableColumn<>("Tokens");
        tokensColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));

        tokensColumn.getStyleClass().addAll("token-col","label");

        TableColumn<String[], String> decisionColumn = new TableColumn<>("Decision");
        decisionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));

        TableColumn<String[], String> observationColumn = new TableColumn<>("Observation");
        observationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));

        // Add columns to the table
        parseTable.getColumns().add(stackColumn);
        parseTable.getColumns().add(tokensColumn);
        parseTable.getColumns().add(decisionColumn);
        parseTable.getColumns().add(observationColumn);
        for (TableColumn<?, ?> column : parseTable.getColumns()) {
            column.setResizable(false);
            column.setReorderable(false);
            column.setSortable(false);
        }
        setHeaderStyle(analysisTable, stackColumn,0);
        setHeaderStyle(analysisTable, tokensColumn,1);
        setHeaderStyle(analysisTable, decisionColumn,2);
        setHeaderStyle(analysisTable, observationColumn,3);
        // Convert the Deque to ObservableList and set it as the table's items
        ObservableList<String[]> data = FXCollections.observableArrayList(analysisTable.getTable());
        parseTable.setItems(data);
        parseTable.setClip(roundedListview());
    }
}
