package popup;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 
 * This class displays a PopUp window 
 * 
 * @author rafaelelkoby
 *
 */
public class PopUp {

	/**
	 * Displays a popup window and disable background use of the source window
	 * 
	 * @param Titel   The popup titel
	 * @param message the messege within the window
	 */
	public static void display(String titel, String message) {

		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PopUpWinController.class.getResource("PopUpWin.fxml"));
			Pane root = loader.load();
			Scene sc = new Scene(root);
			primaryStage.setTitle(titel);
			PopUpWinController popUpWinController = loader.getController();
			popUpWinController.setMessage(message);
			primaryStage.setScene(sc);
			primaryStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
