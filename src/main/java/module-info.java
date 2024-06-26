module com.py.javaf1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;

    opens com.py.javaf1.View to javafx.fxml;

    exports com.py.javaf1;
    exports com.py.javaf1.Controller;
    opens com.py.javaf1.domain to javafx.base, javafx.fxml;
    opens com.py.javaf1.Controller to javafx.fxml;
}