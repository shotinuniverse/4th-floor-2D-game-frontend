module com.shotinuniverse.fourthfloorgamefrontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.shotinuniverse.fourthfloorgamefrontend to javafx.fxml;
    exports com.shotinuniverse.fourthfloorgamefrontend;
    exports com.shotinuniverse.fourthfloorgamefrontend.menu;
    opens com.shotinuniverse.fourthfloorgamefrontend.menu to javafx.fxml;
    exports com.shotinuniverse.fourthfloorgamefrontend.common;
    opens com.shotinuniverse.fourthfloorgamefrontend.common to javafx.fxml;
}