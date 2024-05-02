module com.compiler.lexicalanalyzer {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires jdk.unsupported.desktop;


    opens com.compiler.lexicalanalyzer to javafx.fxml;
    exports com.compiler.lexicalanalyzer;
}