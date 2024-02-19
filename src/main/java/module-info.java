module com.shotinuniverse.fourthfloorgamefrontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.shotinuniverse.fourthfloorgamefrontend to javafx.fxml;
    exports com.shotinuniverse.fourthfloorgamefrontend;
}