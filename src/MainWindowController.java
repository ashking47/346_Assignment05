import javafx.fxml.*;
import javafx.scene.control.*;

import java.util.ArrayList;

public class MainWindowController{
    @FXML
    TextField textField;
    TableView table;
    ArrayList<String> columns = new ArrayList<String>();


    private Main main;
    public void setMain(Main main){
        this.main = main;
    }

    @FXML
    public void search(){
        ArrayList<String> fields = new ArrayList<>();
        String temp = textField.getText();
        fields.add(temp);
        for(int i = 0; i < fields.size(); i++){
            columns.add(i, fields.get(i));
        }
    }

}
