package quynh;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SearchFormController implements Initializable {

    @FXML
    private TableView<Course> tableCourse;
    @FXML
    private TableColumn<Course, String> colId;
    @FXML
    private TableColumn<Course, String> colTitle;
    @FXML
    private TableColumn<Course, Integer> colCredit;
    @FXML
    private TableColumn<Course, String> colCategory;
    @FXML
    private ToggleGroup toggleButtonSearch;
    @FXML
    private TextField textFieldSearch;
    @FXML
    private Button buttonSearch;
    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonSelect;

    private Stage stage;

    private CourseInventoryModel model;
    @FXML
    private RadioButton radioID;
    @FXML
    private RadioButton radioTitle;

    String method = new String();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        radioID.setSelected(true);
    }

    public void setModel(CourseInventoryModel model) {
        this.model = model;
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
    private void handleButtonCancel(ActionEvent event) {
        model.setSelectedCourse(null);
        tableCourse.getItems().clear();
        stage.close();
    }

    @FXML
    private void handleButtonSelect(ActionEvent event) {

        // extract ID only from tableView     
        Course course = tableCourse.getSelectionModel().getSelectedItem();
        model.setSelectedCourse(course);

        stage.close(); // close this window 
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    private void handleButtonSearch(ActionEvent event) {
        
        ObservableList<Course> items = tableCourse.getItems();
        items.clear();// must clear the prev 

        // get search keyword
        String key = textFieldSearch.getText();
        
        ArrayList<Course> courses = new ArrayList<>();
        
        // search method based on the user choice
        if (radioID.isSelected()) {
            if (key == null || key.length() == 0) {
                return;
            }
            courses = model.findCoursesById(key);
        } else if (radioTitle.isSelected()) {
            if (key == null || key.length() == 0) {
                return;
            }
            courses = model.findCourseByTitle(key);
        }

        // search by the keyword and add courses to the table view     
        for (int i = 0; i < courses.size(); ++i) {
            Course course = courses.get(i);
            items.add(course);
        }

        tableCourse.setItems(items);

        colId.setCellValueFactory(new PropertyValueFactory<Course, String>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Course, String>("title"));
        colCredit.setCellValueFactory(new PropertyValueFactory<Course, Integer>("credit"));
        colCategory.setCellValueFactory(new PropertyValueFactory<Course, String>("category"));
    }
}
