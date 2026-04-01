module com.example.projjjjj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.projjjjj to javafx.fxml;
    exports com.example.projjjjj;
}