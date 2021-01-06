package popup;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * 
 * This class is displaying a alertbox type of PopUp window. 
 * 
 * @author rafaelelkoby
 *
 */
public class AlertBox {
	
	/**
	 *
	 * This method displays a alertbox and returns the chosen button
	 *
	 * @param setTitle the alertbox title
	 * @param setHeaderText the alertbox header
	 * @param setContentText the alertbox content
	 * @return true, if OK was clicked 
	 * false, if cancel was clicked
	 */
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
