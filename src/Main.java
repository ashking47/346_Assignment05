import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application{

    AnchorPane pane;
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        mainWindow();
    }

    public void mainWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("applicaton.fxml"));
            pane = loader.load();
            MainWindowController mwc = loader.getController();
            mwc.setMain(this);
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
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
