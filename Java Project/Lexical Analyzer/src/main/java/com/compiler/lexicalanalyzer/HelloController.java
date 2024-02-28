package com.compiler.lexicalanalyzer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HelloController implements Initializable {

    FileChooser fileChooser = new FileChooser();

    @FXML
    private TextArea codeArea;

    @FXML
    void readCode(MouseEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            Scanner scanner = new Scanner(file);
            int i =1;
            while (scanner.hasNextLine()) {
                codeArea.appendText(i+"\t"+ scanner.nextLine()+"\n");
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser.setInitialDirectory(new File("D:\\Maeen\\UNI Documents\\OneDrive - Faculty of Engineering Ain Shams University\\Junior\\Spring 2024\\Design of Compilers\\Project\\Design-Of-Compiler"));
    }
}