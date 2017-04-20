package clark.king.lawrence;

import com.sun.org.apache.xml.internal.security.Init;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable{
    @FXML
    TextField searchTextField, tableTextField;
    @FXML
    ComboBox searchByComboBox;
    @FXML
    TextArea textArea;
    String toTextArea = "";
    int i = 1;

    public void openConnection() throws SQLException{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            String serverName = "csor12c.dhcp.bsu.edu";
            String portNumber = "1521";
            String sid = "or12cdb";
            String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
            Connection conn = DriverManager.getConnection(url, "aeking2", "7661");
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(search());
            int rowcount = 0;
            ResultSetMetaData rsmd = rset.getMetaData();
            int colunm = rsmd.getColumnCount();
                while (rset.next()) {
                    for(int i = 1; i < colunm; i++) {
                        toTextArea += (rset.getString(i) + " ");

                   }
                }
                toTextArea+="\n";
                textArea.setText(toTextArea );
            }


    ArrayList<String> SQLText = new ArrayList<>();
    List<String> tables = Arrays.asList("Case", "Active Case", "Resolved Case", "Child", "Suspect", "Vehicle");
    List<String> caseColumns = Arrays.asList("Case ID");
    List<String> activeCaseColumns = Arrays.asList();
    List<String> searchBy = Arrays.asList("Child First Name", "Child Last Name", "Suspect First Name", "Suspect Last Name", "Case ID", "State", "City");






    @FXML
    public void populateSearchByComboBox(){
        ObservableList<String> searchByList = FXCollections.observableList(searchBy);
        searchByComboBox.setItems(searchByList);

    }

    @FXML
    public String search() {
        String constraint = searchByComboBox.getSelectionModel().getSelectedItem().toString();
        String searchField = searchTextField.getText();
        if (constraint.equals("Child First Name") || constraint.equals("Child Last Name") || constraint.equals("City") || constraint.equals("State") || constraint.equals("Case ID")) {
            if (constraint.contains("First Name")) {
                return ("select * from child where FNAME = '" + searchField +"'");
            }
            if (constraint.contains("Last Name")) {
                return ("select * from child where LNAME = '" + searchField +"'");
            }
            if (constraint.contains("City")) {
                return ("select * from child where MCity = '" + searchField +"'");
            }
            if (constraint.contains("State")) {
                return ("select * from child where MState =  '" + searchField +"'");
            } else {
                return ("select * from child where CID =  '" + searchField +"'");
            }
        } else {
            if(constraint.contains("Last Name")){
                return ("select * from suspect where LNAME =  '" + searchField +"'");
            }
            if(constraint.contains("First Name")){
                return ("select * from suspect where FNAME =  '" + searchField +"'");
            }
            if(constraint.contains("Case ID")){
                return ("select * from suspect where CID =  '" + searchField +"'");
            }
        }
        return "";
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateSearchByComboBox();

    }
}
