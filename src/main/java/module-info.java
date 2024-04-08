module app.photoapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;

    opens app.photoapplication to javafx.fxml;
    exports app.photoapplication;
    exports app.photoapplication.model;
    opens app.photoapplication.model to javafx.fxml;
    exports app.photoapplication.controller;
    opens app.photoapplication.controller to javafx.fxml;
}