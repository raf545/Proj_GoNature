package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainPageEmployeeController extends Application {
    @FXML
    private Pane mainPane;

    @FXML
    private ImageView openNewFamilySubBtnP;

    @FXML
    private Button NewFamilySubBtn;

    @FXML
    private ImageView openNewInstructorBtnP;

    @FXML
    private Button NewInstructorBtn;

    @FXML
    private Rectangle quitBtn;

    
    public static void main(String[] args) {
		launch(args);
	}
    
    @Override
    public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainPageEmployee.fxml"));				
			primaryStage.setTitle("Main Page Employee");
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);

			primaryStage.show();

			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
    
    
    
    
    
    @FXML
    void openNewFamilySub(ActionEvent event) throws IOException  {
//    	FXMLLoader loader = new FXMLLoader();
//		loader.setLocation(this.getClass().getResource("newfamilysubworker.fxml"));
//
//		Pane root = loader.load();
//		mainPane.getChildren().clear();
//		mainPane.getChildren().add(root);
    	
    	try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) quitBtn.getScene().getWindow();
			// FIXME primaryStage.setOnCloseRequest(value);
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("newfamilysubworker.fxml"));
			Pane root = loader.load();
			Scene sc = new Scene(root);
			primaryStage.setTitle("Main page");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}

    }

    @FXML
    void openNewInstructor(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("newGroupLeaderworkerns.fxml"));

		Pane root = loader.load();
		mainPane.getChildren().clear();
		mainPane.getChildren().add(root);
    }

    @FXML
    void quit(MouseEvent event) {
    	// get a handle to the stage
    			Stage stage = (Stage) quitBtn.getScene().getWindow();
    			// do what you have to do
    			stage.close();
    			System.exit(0);


    }

	

}
