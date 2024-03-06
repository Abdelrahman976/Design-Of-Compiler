module com.compiler.lexicalanalyzer {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;


    opens com.compiler.lexicalanalyzer to javafx.fxml;
    exports com.compiler.lexicalanalyzer;
}