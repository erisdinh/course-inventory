package quynh;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddFormController implements Initializable {

    @FXML
    private TextField textFieldID;
    @FXML
    private TextField textFieldTitle;
    @FXML
    private TextField textFieldCredit;
    @FXML
    private ComboBox<String> comboBoxCategory;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonCancel;

    private Stage stage;
    private CourseInventoryModel model;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
            
            // prompt user to confirm
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to close it?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                // User click OK, close the application
                stage.close();
            } else {

                // eat tup the close event
                e.consume();
            }
        });
    }

    @FXML
    private void handleButtonAdd(ActionEvent event) {
        
        Alert alert = new Alert(AlertType.ERROR);

        // validate ID     
        String id = textFieldID.getText().toUpperCase();
        if (model.validateId(id) == false) {
            alert.setContentText("ID must be 4 alphabets followed by 5 digit");
            alert.show();
            return;
        }

        // validate title     
        String title = textFieldTitle.getText();
        if (title == null || title.length() == 0) {
            alert.setContentText("Title cannot be empty.");
            alert.show();
            return;
        }

        // validate credit     
        int credit = 0;
        try {
            credit = Integer.parseInt(textFieldCredit.getText());
        } catch (NumberFormatException e) {
            alert.setContentText("Credit must be numeric.");
            alert.show();
            return;
        }
        if (credit <= 0) {
            alert.setContentText("Credit must be greater than 0.");
            alert.show();
            return;
        }

        // validate category     
        String cat = comboBoxCategory.getValue();
        if (cat == null) {
            alert.setContentText("Please choose a category.");
            alert.show();
            return;
        }

        // add course and close this window     
        model.addCourse(id, title, credit, cat);
        stage.close();
    }

    @FXML
    private void handleButtonCancel(ActionEvent event) {
        
        // prompt user to confirm
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to close it?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // User click OK, close the application
            stage.close();
        } else {
            event.consume();
        }

        if (stage != null) {
            stage.close();
        }
    }
    
    public void setModel(CourseInventoryModel model) {     
        this.model = model;     
        comboBoxCategory.setItems(FXCollections.observableList(model.getCategories()));
    }
}
