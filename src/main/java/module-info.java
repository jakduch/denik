module com.example.denik {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.denik to javafx.fxml;
    exports com.example.denik;
}