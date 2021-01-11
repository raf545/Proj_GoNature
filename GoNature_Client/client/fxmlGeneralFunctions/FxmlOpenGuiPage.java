package fxmlGeneralFunctions;

import java.io.IOException;

import guiCommon.StaticPaneMainPageClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mainVisitorPage.MainPageForClientController;

public class FxmlOpenGuiPage implements IFxmlOpenGuiPage {
	/**
	 * loads a fxml file with a given name and setes the window titel
	 * 
	 * @throws IOException
	 */

	@Override
	public void openPageUsingFxmlName(String VisitorHelloString, String VisitorType, Text text) {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) text.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageForClientController.class.getResource("MainPageForClient.fxml"));
			Pane root = loader.load();

			MainPageForClientController mainPageForClientController = loader.getController();
			mainPageForClientController.setTitels("hello " + VisitorHelloString, VisitorType);
			StaticPaneMainPageClient.clientMainPane = mainPageForClientController.getPane();

			Scene sc = new Scene(root);
			primaryStage.setOnCloseRequest(e -> FXMLFunctions.closeMainPage());
			primaryStage.setTitle("Main page");
			primaryStage.setScene(sc);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}
}
