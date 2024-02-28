module com.compiler.lexicalanalyzer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.compiler.lexicalanalyzer to javafx.fxml;
    exports com.compiler.lexicalanalyzer;
}