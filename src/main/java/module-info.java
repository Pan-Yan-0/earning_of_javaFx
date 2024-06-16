module org.example.javafxproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.javafxproject to javafx.fxml;
    exports org.example.javafxproject;
}