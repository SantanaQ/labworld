module com.sim {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.unsupported;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires spring.context;
    requires spring.websocket;
    requires java.desktop;

    exports com.sim;
}