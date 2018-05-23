package quynh;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainFormController implements Initializable {

    // remember its stage reference
    private Stage stage;

    // model part of MVC
    private CourseInventoryModel model;

    // create an instance of file
    File file;

    @FXML
    private Button buttonSearch;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private ListView<Course> listViewCourse;
    @FXML
    private TextField TextFieldTitle;
    @FXML
    private TextField TextFieldCredit;
    @FXML
    private ComboBox<String> comboBoxCategory;
    @FXML
    private GridPane paneEdit;
    @FXML
    private MenuItem menuAdd;
    @FXML
    private MenuItem menuDelete;
    @FXML
    private MenuItem menuEdit;
    @FXML
    private MenuItem menuAbout;
    @FXML
    private MenuItem menuSave;
    @FXML
    private ComboBox<String> comboFilterCategory;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // create model object
        model = new CourseInventoryModel();

        // open courses.dat as a default file
        file = new File("courses.dat");
        model.readCourseFile(file);

        comboFilterCategory.getItems().add("All Categories");
        comboFilterCategory.getSelectionModel().selectFirst();
        if (model.getCourseCount() > 0) {

            // convert ArrayList to ObservableList then assign it
            listViewCourse.setItems(FXCollections.observableList(model.getCourses()));
            model.populateCategories();
            comboFilterCategory.getItems().addAll(FXCollections.observableList(model.getCategories()));
            comboBoxCategory.setItems(FXCollections.observableList(model.getCategories()));
        }

        // declare anonymous inner class extended listCell
        // and override updateItem()
        listViewCourse.setCellFactory(listview -> {
            ListCell<Course> cell = new ListCell<Course>() {

                // override updateItem()
                public void updateItem(Course course, boolean empty) {
                    super.updateItem(course, empty);
                    // check if course is null as well
                    if (empty || course == null) {
                        this.setText(null);
                        this.setGraphic(null);
                    } else {
                        this.setText(course.getId());
                    }
                }
            };
            return cell;
        });

        // when user selects a course, show the information about the course
        listViewCourse.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> {
            showCourseInfo(newValue);
        });

        // get courses by category
        comboFilterCategory.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            ArrayList<Course> courses = model.findCourseByCategory(newValue);
            listViewCourse.setItems(FXCollections.observableList(courses));
        });
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
                Platform.exit();
            } else {

                // eat tup the close event
                e.consume();
            }
        });
    }

    @FXML
    private void handleMenuOpen(ActionEvent event) {

        // create fileChooser dialog
        // pass stage to the controller
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open course List File...");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("DAT", "*.dat"),
                new FileChooser.ExtensionFilter("TXT", "*.txt"));

        file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }
        // open the file
        model.readCourseFile(file);

        // add Ids to listView
        if (model.getCourseCount() > 0) {

            // convert ArrayList to ObservableList then assign it
            listViewCourse.setItems(FXCollections.observableList(model.getCourses()));
            model.populateCategories();
            comboFilterCategory.getItems().addAll(FXCollections.observableList(model.getCategories()));
            comboBoxCategory.setItems(FXCollections.observableList(model.getCategories()));

            // enable the buttons
            buttonAdd.setDisable(false);
            buttonDelete.setDisable(false);
            buttonEdit.setDisable(false);
            buttonSearch.setDisable(false);
            buttonSave.setDisable(false);
            buttonCancel.setDisable(false);
            listViewCourse.setDisable(false);
            comboFilterCategory.setDisable(false);
        } else {
            buttonAdd.setDisable(true);
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
            buttonSearch.setDisable(true);
            buttonSave.setDisable(true);
            buttonCancel.setDisable(true);
            listViewCourse.setDisable(true);
            paneEdit.setDisable(true);
            comboFilterCategory.setDisable(true);
        }
    }

    @FXML
    private void handleMenuSaveAs(ActionEvent event) {

        // open a diaglog for th user to save the course inventory in the new file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Course List File As...");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("DAT", "*.dat"),
                new FileChooser.ExtensionFilter("TXT", "*.txt"));
        file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }
        model.saveCourseFile(file);
    }

    @FXML
    private void handleMenuClose(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to close?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    @FXML
    private void handleMenuAbout(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutForm.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        // create and show about window as modal     
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("About");
        stage.setScene(scene);
        stage.show();

        // remember the stage, so can close it later     
        AboutFormController ctrlAbout = fxmlLoader.getController();
        ctrlAbout.setStage(stage);
    }

    @FXML
    private void handleButtonSearch(ActionEvent event) throws Exception {

        // load scence graph from fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SearchForm.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        //create and show about window as modal
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Search Course");
        stage.setScene(scene);

        // remeber the stage, so can close it later
        SearchFormController controller = fxmlLoader.getController();

        controller.setStage(stage);
        controller.setModel(model);

        // show modal and wait
        stage.showAndWait();

        // update listView with the user selection
        Course course = model.getSelectedCourse();
        int index = model.getSelectedCourseIndex();

        // there is a selected course
        if (course != null) {
            // programmatically select an item in the listView
            listViewCourse.getSelectionModel().select(course);

            // make it focused
            listViewCourse.getFocusModel().focus(index);
            listViewCourse.scrollTo(index);
        } else {
            listViewCourse.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleButtonEdit(ActionEvent event) {

        // enable controls     
        paneEdit.setDisable(false);
    }

    @FXML
    private void handleButtonDelete(ActionEvent event) {
        // get selected course from listview     
        Course course = listViewCourse.getSelectionModel().getSelectedItem();
        if (course == null) {
            return;
        }

        // confirm deletion using alert     
        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to delete " + course.getId() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            // delete the course and save to the file         
            model.removeCourse(course);
            model.saveCourseFile(file);

            // reload course list         
            listViewCourse.setItems(FXCollections.observableList(model.getCourses()));
            listViewCourse.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleButtonAdd(ActionEvent event) throws Exception {

        // load scence graph from fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddForm.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        //create and show about window as modal
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add New Course");
        stage.setScene(scene);

        // remember the stage, so can close it later
        AddFormController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setModel(model);

        // wait user response within this block
        stage.showAndWait();

        // process the user response here     
        String id = model.getNewCourseId();
        int index = model.getCourseIndex(id);
        if (id == null) {
            return;
        }

        // save the file     
        model.saveCourseFile(file);

        // focus on the new item     
        listViewCourse.setItems(FXCollections.observableList(model.getCourses()));

        // re-populate listView      
        listViewCourse.getSelectionModel().select(index);
        listViewCourse.getFocusModel().focus(index);
        listViewCourse.scrollTo(index);
    }

    @FXML
    private void handleButtonMenuSave(ActionEvent event) {

        // get selected course from listview     
        Course c = listViewCourse.getSelectionModel().getSelectedItem();
        if (c == null) {
            return;
        }

        // confirm saving     
        Alert alert1 = new Alert(AlertType.CONFIRMATION, "Do you want to save the changes?");
        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }

        // alert for validation     
        Alert alert2 = new Alert(AlertType.ERROR);

        // validate title     
        String title = TextFieldTitle.getText();
        if (title == null || title.length() == 0) {
            alert2.setContentText("itle cannot be empty.");
            alert2.show();
            return;
        }

        // validate credit     
        int credit;
        try {
            credit = Integer.parseInt(TextFieldCredit.getText());
        } catch (NumberFormatException e) {
            alert2.setContentText("Credit must be an integer.");
            alert2.show();
            return;
        }
        if (credit < 0) {
            alert2.setContentText("Credit must be greater than 0.");
            alert2.show();
            return;
        }

        // validate category     
        String cat = comboBoxCategory.getValue();
        if (cat == null) {
            alert2.setContentText("Category must be selected.");
            alert2.show();
            return;
        }

        // finally, update the course     
        model.updateCourse(c.getId(), title, credit, cat);
        model.saveCourseFile(file);
        paneEdit.setDisable(true);
    }

    @FXML
    private void handleButtonCancel(ActionEvent event) {

        // enable/disable controls     
        paneEdit.setDisable(true);
    }

    private void showCourseInfo(Course course) {
        if (course == null) {
            return;
        }
        TextFieldTitle.setText(course.getTitle());
        TextFieldCredit.setText(String.valueOf(course.getCredit()));
        comboBoxCategory.getSelectionModel().select(course.getCategory());

        paneEdit.setDisable(true);
    }
}
