package employee;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import familySubWorker.NewFamilySubWorkerController;
import fxmlGeneralFunctions.FXMLFunctions;
import instructorSubWorker.NewInstructorSubWorkerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.GoNatureLoginController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import reservation.ReservationForOccasionalVisitorController;

/**
 * The main page for an employee,contains all the methods that employee can do
 * 
 * @author ziv
 *
 */
public class MainPageEmployeeController {

	 @FXML
	 private Text emppark;
	
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
	private Label manPageEmpName;

	@FXML
	private Hyperlink logoutBtn;
	
	 @FXML
	 private Label capacityText;

	Gson gson = new Gson();

	private Employee employee;

	/**set the changing text for the main.
	 * @param employeeFromDB
	 * @throws IOException
	 */
	public void setEmp(Employee employeeFromDB) throws IOException {
		employee = employeeFromDB;
		FXMLFunctions.loadSceneToMainPane(BlankEmployeeController.class, "BlankEmployee.fxml", mainPane);
		manPageEmpName.setText("Hello " + employeeFromDB.getName() + " " + employeeFromDB.getLasstName());
		emppark.setText(employee.getParkName()+" Employee" );
	}
	
	/**asks from the server for current capacity of the park.
	 * 
	 */
	public void getAmountOfPeopleTodayInPark()
	{
		RequestHandler rh = new RequestHandler(controllerName.EmployeeSystemController, "getAmountOfPeopleTodayInPark", employee.getParkName());
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
		
	}	
	
	/**handle the massage from the server.
	 * 
	 */
	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;		
		if(!answer.equals("faild"))
			setCapacityTextField(answer);
		
	}

	/**prints the capacity on main
	 * @param answer capacity from the server.
	 */
	private void setCapacityTextField(String answer) {
		System.out.println("Shay");
		String parkCapacities = answer;
		capacityText.setText(employee.getParkName() +" capacity is: "+ parkCapacities );
		
	}

	/**get the  pane
	 * @return
	 */
	public Pane getPane() {
		return mainPane;
	}

	/**
	 * opens new subscription page for making new subscribers.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void openNewFamilySub(ActionEvent event) throws IOException {
//		FIXME
//		StaticPaneMainPageEmployee.employeeMainPane = mainPane;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NewFamilySubWorkerController.class.getResource("newfamilysubworker.fxml"));

		Pane root = loader.load();
		mainPane.getChildren().clear();
		mainPane.getChildren().add(root);

	}

	/**
	 * opens new instructor page for making new instructors.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void openNewInstructor(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NewInstructorSubWorkerController.class.getResource("newInstructorSubWorker.fxml"));

		Pane root = loader.load();
		mainPane.getChildren().clear();
		mainPane.getChildren().add(root);
	}

	/**
	 * Go back to the login page when pressed in order to log in as another user.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void logout(ActionEvent event) throws IOException {
		RequestHandler rh = new RequestHandler(controllerName.LoginController, "logout", "");
		ClientUI.chat.accept(gson.toJson(rh));

		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) NewInstructorBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GoNatureLoginController.class.getResource("GoNatureLogin.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Sign In");
		primaryStage.setScene(sc);
		primaryStage.show();

	}

	/**
	 * Go to reservation page for occasional visitors that came to the park at a
	 * specific time without reservation.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void reservationForOccasionalVisitor(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(
				ReservationForOccasionalVisitorController.class.getResource("ReservationForOccasionalVisitor.fxml"));
		Pane root = loader.load();
		ReservationForOccasionalVisitorController reservationForOccasionalVisitorController = loader.getController();
		reservationForOccasionalVisitorController.setIdentFields();
		mainPane.getChildren().clear();
		mainPane.getChildren().add(root);
	}

}