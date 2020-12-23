package fxmlGeneralFunctions;

import java.io.IOException;

import com.google.gson.Gson;

import client.ClientUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import login.GoNatureLoginController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class FXMLFunctions {

	static Gson g = new Gson();
	//In the main page of each user there is a main blank page that we fill with a FXML file.
	//This function will do it for us
	//The function return the loader in order to help us change details in the scene before we load it.
	//For example, loading combo boxes through loader will be loader.getController().useFunction().
	
	public static FXMLLoader loadSceneToMainPane(Class controllerClass, String fxmlName,Pane mainPane) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(controllerClass.getResource(fxmlName));
		Pane root = loader.load();
		try {
			mainPane.getChildren().clear();
			mainPane.getChildren().add(root);
		} catch (Exception e) 
		{
		}
		return loader;
	}
	
	//Logout from each of the Main Pages, send the current scene to escape it.
	public static void logOutFromMainPage(Scene myScene) throws IOException
	{
		RequestHandler rh = new RequestHandler(controllerName.LoginController, "logout", "");
		ClientUI.chat.accept(g.toJson(rh));

		Stage primaryStage = new Stage();
		Stage stage = (Stage) myScene.getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GoNatureLoginController.class.getResource("GoNatureLogin.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Sign In Employee");
		primaryStage.setScene(sc);
		primaryStage.show();
	}
	
}