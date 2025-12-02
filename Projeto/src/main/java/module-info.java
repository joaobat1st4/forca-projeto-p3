module com.example.projeto {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.projeto to javafx.fxml;
    exports com.example.projeto;
}