module com.shotinuniverse.fourthfloorgamefrontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires jackson.databind;

    opens com.shotinuniverse.fourthfloorgamefrontend to javafx.fxml;
    exports com.shotinuniverse.fourthfloorgamefrontend;
}