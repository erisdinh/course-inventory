package quynh;

import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CourseInventory_QuynhDinh extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Get the controller pass the FXML file to FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
        Parent root = (Parent) loader.load();
        // Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Course Inventory");
        
        // set icon for course inventory
        stage.getIcons().add(new Image(CourseInventory_QuynhDinh.class.getResourceAsStream("course.png")));

        // assign stage to the controller
        MainFormController controller = (MainFormController) loader.getController();
        controller.setStage(stage);

        // show window
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
