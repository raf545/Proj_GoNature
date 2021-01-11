package fxmlGeneralFunctions;

import java.io.IOException;

import javafx.stage.Stage;

public interface IFxmlOpenGuiPage {

	/**
	 * loads a fxml file with a given name and setes the window titel
	 * 
	 * @throws IOException
	 */

	void openPageUsingFxmlName(String VisitorHelloString, String VisitorType, Stage stage);

}