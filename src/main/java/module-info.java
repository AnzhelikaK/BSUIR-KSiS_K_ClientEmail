module com.bsuir {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires net.rgielen.fxweaver.core;
    requires spring.beans;
    requires lombok;
    requires org.controlsfx.controls;
    requires org.slf4j;
    requires mail;

    opens com.bsuir to javafx.fxml, spring.core;
    opens com.bsuir.service to spring.beans;
    opens com.bsuir.map to spring.beans;
    opens com.bsuir.controller.map to spring.beans;
    opens com.bsuir.controller.dto to javafx.base;
    opens com.bsuir.controller to javafx.fxml;

    exports com.bsuir;
    exports com.bsuir.controller;
}
