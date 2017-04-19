package clark.king.lawrence;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;

import java.util.ArrayList;

public class MainWindowController{
    @FXML
    TextField textField;
    @FXML
    TableView table;
    @FXML
    ArrayList<String> columns = new ArrayList<String>();


    @FXML
    public void search(){
        ArrayList<String> fields = new ArrayList<>();
        String temp = textField.getText();
        fields.add(temp);
        for(int i = 0; i < fields.size(); i++){
            columns.add(i, fields.get(i));
        }
        TableColumn <String,String> column = new TableColumn<>(textField.getText());
        column.setCellValueFactory(new MapValueFactory("test"));
        column.setMinWidth(130);
        table.getColumns().add(column);
    }

}
