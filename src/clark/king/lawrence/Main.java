package clark.king.lawrence;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
        primaryStage.setTitle("Cardinal Docs: Classroom Edition");
        primaryStage.setScene(new Scene(root, 820, 400));
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);


    /*    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

        String serverName = "csor12c.dhcp.bsu.edu";
        String portNumber = "1521";
        String sid = "or12cdb";
        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;

        Connection conn = DriverManager.getConnection(url, "aeking2", "7661");

        System.out.println("Oracle or 12cbd is connected.");

        Statement stmt = conn.createStatement();
        */



    }

}
