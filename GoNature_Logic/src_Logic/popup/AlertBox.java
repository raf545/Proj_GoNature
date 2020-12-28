package popup;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertBox {
	
	public static boolean display(String setTitle,String setHeaderText,String setContentText) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(setTitle);
		alert.setHeaderText(setHeaderText);
		alert.setContentText(setContentText);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}
}
