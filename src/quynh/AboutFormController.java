package quynh;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class AboutFormController implements Initializable {

    @FXML
    private Button buttonClose;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleButtonClose(ActionEvent event) {
        if(stage != null)         
            stage.close(); 
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
    }  
}
