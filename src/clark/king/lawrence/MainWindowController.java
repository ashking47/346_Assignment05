package clark.king.lawrence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable{
    @FXML
    TextField searchTextField;
    @FXML
    ComboBox searchByComboBox;
    @FXML
    TextArea textArea;

    String toTextArea = "";
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private int count;


    List<String> caseColumns = Arrays.asList("Case ID");
    List<String> activeCaseColumns = Arrays.asList();
    List<String> searchBy = Arrays.asList("Child First Name", "Child Last Name", "Suspect First Name", "Suspect Last Name", "Case ID", "State", "City");


    public void establishConnection(String input) throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        String serverName = "csor12c.dhcp.bsu.edu";
        String portNumber = "1521";
        String sid = "or12cdb";
        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
        conn = DriverManager.getConnection(url, "aeking2", "7661");
        stmt = conn.createStatement();
        rset = stmt.executeQuery(input);
        ResultSetMetaData rsmd = rset.getMetaData();
        count = rsmd.getColumnCount();
        while (rset.next()) {
            for (int i = 1; i < count; i++) {
                toTextArea += (rset.getString(i) + " ");
            }
        }
        toTextArea += "\n";
        textArea.setText(toTextArea);
        rset.close();
        stmt.close();
        conn.close();
    }


    public int setCount() throws SQLException {
        ResultSetMetaData rsmd = rset.getMetaData();
        count = rsmd.getColumnCount();
        return count;
    }

    public void createQuery(String query) throws SQLException {

    }

    @FXML
    public void populateSearchByComboBox(){
        ObservableList<String> searchByList = FXCollections.observableList(searchBy);
        searchByComboBox.setItems(searchByList);
    }

   /*public SQLManager getSQLManager(){
        return mySQL;
    }
    public void getQuery() throws SQLException {
        ResultSet rset = mySQL.createQuery(search());
        ResultSetMetaData rsmd = rset.getMetaData();
        int column = rsmd.getColumnCount();
        while (rset.next()) {
            for (int i = 1; i < column; i++) {
                toTextArea += (rset.getString(i) + " ");

            }
        }
        toTextArea += "\n";
        textArea.setText(toTextArea);

    }
*/
    @FXML
    public void search() throws SQLException {
        String constraint = searchByComboBox.getSelectionModel().getSelectedItem().toString();
        String searchField = searchTextField.getText();
        if (constraint.equals("Child First Name") || constraint.equals("Child Last Name") || constraint.equals("City") || constraint.equals("State") || constraint.equals("Case ID")) {
            if (constraint.contains("First Name")) {
                establishConnection("select * from child where FNAME = '" + searchField +"'");
            }
            if (constraint.contains("Last Name")) {
                establishConnection("select * from child where LNAME = '" + searchField +"'");
            }
            if (constraint.contains("City")) {
                establishConnection("select * from child where MCity = '" + searchField +"'");
            }
            if (constraint.contains("State")) {
                establishConnection("select * from child where MState =  '" + searchField +"'");
            } else {
                establishConnection("select * from child where CID =  '" + searchField +"'");
            }
        } else {
            if(constraint.contains("Last Name")){
                establishConnection("select * from suspect where LNAME =  '" + searchField +"'");
            }
            if(constraint.contains("First Name")){
                establishConnection("select * from suspect where FNAME =  '" + searchField +"'");
            }
            if(constraint.contains("Case ID")){
                establishConnection("select * from suspect where CID =  '" + searchField +"'");
            }
        }
    }

    @FXML
    public void calculateAge() throws SQLException {
        establishConnection("select fname, lname, (to_number(to_char(SYSDATE, 'YYYY')) " +
        "- to_number(to_char(BDATE, 'YYYY'))), gender, MCity, MState from child");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateSearchByComboBox();

    }
}
