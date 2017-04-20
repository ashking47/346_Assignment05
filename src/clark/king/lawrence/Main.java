package clark.king.lawrence;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.*;
import java.sql.*;
import oracle.jdbc.OracleDriver;

public class Main extends Application {
    MainWindowController mwc = new MainWindowController();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
        primaryStage.setTitle("346 Assignment 5");
        primaryStage.setScene(new Scene(root, 991, 618));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }







}