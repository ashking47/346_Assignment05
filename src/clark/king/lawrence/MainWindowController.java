package clark.king.lawrence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.net.URL;
import java.sql.*;
import java.util.*;


public class MainWindowController implements Initializable{
    @FXML
    TextField searchTextField;
    @FXML
    ComboBox searchByComboBox, missingDatesComboBox;
    @FXML
    TextArea textArea;
    int mnCount = 0;
    int wiCount= 0;
    int ohCount= 0;
    int tnCount= 0;
    int miCount= 0;
    int ilCount= 0;
    int coCount= 0;
    int kaCount= 0;
    int inCount= 0;
    int moCount= 0;
    int kyCount= 0;

    HashMap<String, Double> stateCounts = new HashMap<>();
    String toTextArea = "";
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private int count;

    List<String> datesMissing = Arrays.asList("1995 - 2000", "2001 - 2005", "2006 - 2010", "2011 - 2017");
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
        count = rsmd.getColumnCount()+1;
        String output = getToTextArea(rset, count);
        stateCounts = stateMap(rset, count);
        textArea.setText(output);
        rset.close();
        stmt.close();
        conn.close();
    }



    public String getToTextArea(ResultSet rset, int count) throws SQLException {
        while (rset.next()) {
            for (int i = 1; i < count; i++) {
                toTextArea += (rset.getString(i) + " ");
            }
            toTextArea += "\n";
        }
        return toTextArea;
    }

    public HashMap<String, Double> stateMap(ResultSet rset, int count) throws SQLException {
        while (rset.next()) {
            for (int i = 1; i < count; i++) {
                toTextArea += (rset.getString(i) + " ");
            }
            stateCounts = stateRatios(toTextArea);
        }
        return stateCounts;
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

    @FXML
    public void populateDatesComboBox(){
        ObservableList<String> datesList = FXCollections.observableList(datesMissing);
        missingDatesComboBox.setItems(datesList);
    }

    @FXML
    public void findChidrenByDate(){}

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

    @FXML
    public void timeOfCaseOpened() throws SQLException{
        establishConnection("select cid, lname, fname, (to_number(to_char(SYSDATE, 'YYYY')) " +
                "- to_number(to_char(BDATE, 'YYYY'))), MCity, MState FROM child order by "+
        "(to_number(to_char(mdate, 'YYYY')) - to_number(to_char(bdate, 'YYYY'))) ASC");
    }

    @FXML
    public void activeStates() throws SQLException {
        establishConnection("select c.lname, c.fname, c.MSTATE, c.mcity from child c, active_case a"
        + " where c.cid = a.id order by c.mstate");
        stateCounts = stateRatios(textArea.getText());
        statePieChart(stateCounts);
    }

    @FXML
    public void activeVSResolvedCases() throws SQLException {
        textArea.setText("ACTIVE CASES:\n");
        establishConnection("select COUNT(*)  FROM ACTIVE_CASE");
        textArea.setText("\nRESOLVED CASES\n");
        establishConnection("select COUNT(*) FROM RESOLVED_CASE");
        casePieChart();

    }

    public ArrayList<String> splitInput(String input){
        ArrayList<String> list = new ArrayList<>();
        for(String word : input.split(" ")){
            list.add(word);
        } return list;
    }

    @FXML
    public void casePieChart(){
        ArrayList<String> caseList = splitInput(textArea.getText());
        Integer a = Integer.valueOf(caseList.get(0));
        Integer r = Integer.valueOf(caseList.get(1));
        Scene scene = new Scene(new Group());
        Stage stage = new Stage();
        stage.setTitle("Active Cases vs Resolved Cases");
        stage.setWidth(500);
        stage.setHeight(500);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Active", a),
                new PieChart.Data("Resolved", r));
        final PieChart statesChart = new PieChart(pieChartData);
        ((Group) scene.getRoot()).getChildren().add(statesChart);
        stage.setScene(scene);
        stage.show();
    }



    public HashMap<String, Double> stateRatios(String input){
        ArrayList<String> list = splitInput(input);
        //Pattern x = Pattern.compile("[A-Z]{2}");
        for(String element : list) {
            if (element.matches("[A-Z][A-Z]")) {
                if (element.equals("MN")) {
                    //mnCount++;
                    stateCounts.put("MN", +1.0);
                }
                if (element.equals("WI")) {
                    //wiCount++;
                    stateCounts.put("wI", +1.0);
                }
                if (element.equals("OH")) {
                    //ohCount++;
                    stateCounts.put("OH", +1.0);
                }
                if (element.equals("TN")) {
                    //tnCount++;
                    stateCounts.put("TN", +1.0);
                }
                if (element.equals("MI")) {
                    //miCount++;
                    stateCounts.put("MI", +1.0);
                }
                if (element.equals("IL")) {
                    //ilCount++;
                    stateCounts.put("IL", +1.0);
                }
                if (element.equals("CO")) {
                    //coCount++;
                    stateCounts.put("CO", +1.0);
                }
                if (element.equals("KA")) {
                    //kaCount++;
                    stateCounts.put("KA", +1.0);
                }
                if (element.equals("IN")) {
                    //inCount++;
                    stateCounts.put("IN", +1.0);
                }
                if (element.equals("MO")) {
                    //moCount++;
                    stateCounts.put("MO", +1.0);
                }
                if (element.equals("KY")) {
                    //kyCount++;
                    stateCounts.put("KY", +1.0);
                } else {
                    System.out.println("No state found");
               }
            }
        } return stateCounts;
    }

    @FXML
    public void statePieChart(HashMap<String, Double> stateCounts){
        Scene scene = new Scene(new Group());
        Stage stage = new Stage();
        stage.setTitle("States of Missing Children in Active Cases");
        stage.setWidth(500);
        stage.setHeight(500);
        PieChart.Data mn = new PieChart.Data("MN", stateCounts.get("MN"));
        PieChart.Data wi = new PieChart.Data("WI", stateCounts.get("WI"));
        PieChart.Data oh = new PieChart.Data("OH", stateCounts.get("OH"));
        PieChart.Data tn = new PieChart.Data("TN", stateCounts.get("TN"));
        PieChart.Data mi = new PieChart.Data("MI", stateCounts.get("MI"));
        PieChart.Data il = new PieChart.Data("IL", stateCounts.get("IL"));
        PieChart.Data co = new PieChart.Data("CO", stateCounts.get("CO"));
        PieChart.Data ka = new PieChart.Data("KA", stateCounts.get("KA"));
        PieChart.Data in = new PieChart.Data("IN", stateCounts.get("IN"));
        PieChart.Data mo = new PieChart.Data("MO", stateCounts.get("MO"));
        PieChart.Data ky = new PieChart.Data("KY", stateCounts.get("KY"));
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(mn, wi, oh, tn, mi, il, co, ka, in, mo, ky);
        final PieChart statesChart = new PieChart(pieChartData);
        ((Group) scene.getRoot()).getChildren().add(statesChart);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateSearchByComboBox();
        populateDatesComboBox();

    }
}
