module Project03 {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;
    requires javafx.swt;
    requires javafx.controls;
    requires javafx.swing;
    requires javafx.web;

    requires java.sql;
    requires mysql.connector.java;

    opens sample;
    opens controller;
    opens hotel;
}