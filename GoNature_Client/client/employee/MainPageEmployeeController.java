package employee;

import java.io.IOException;

import com.google.gson.Gson;


import cardReaderSimulator.CardReaderControllerSimulator;
import client.ChatClient;
import client.ClientUI;
import familySubWorker.NewFamilySubWorkerController;
import fxmlGeneralFunctions.FXMLFunctions;
import instructorSubWorker.NewInstructorSubWorkerController;
import javafx.application.Platform;
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
import subscriber.Subscriber;

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
	String[] ansback = new String[2];
	Thread thread;
	private Employee employee;

	/**
	 * set the changing text for the main.
	 * 
	 * @param employeeFromDB
	 * @throws IOException
	 */
	public void setEmp(Employee employeeFromDB,Thread thread) throws IOException {
		employee = employeeFromDB;
		this.thread = thread;
		FXMLFunctions.loadSceneToMainPane(BlankEmployeeController.class, "BlankEmployee.fxml", mainPane);
		manPageEmpName.setText("Hello " + employeeFromDB.getName() + " " + employeeFromDB.getLasstName());
		emppark.setText(employee.getParkName() + " Employee");
	}

	/**
	 * perform a update to the window capacity
	 */
	public void performCapacityUpdate(String answer) {
		Platform.runLater(() -> {
			ansback = gson.fromJson(answer, String[].class);
			if (!answer.equals("faild")) {
				String parkCurCapacities = ansback[0];
				capacityText.setText(" The amount of people in park " + employee.getParkName() + " is: "
						+ parkCurCapacities + "/" + ansback[1]);
			}

		});

	}

	/**
	 * get the pane
	 * 
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
	 * and terminate the thread updating the capacity
	 * @param event
	 * @throws IOException
	 */

	@SuppressWarnings("deprecation")
	@FXML
	void logout(ActionEvent event) throws IOException {
		thread.stop();
		FXMLFunctions.logOutFromMainPage(logoutBtn.getScene());

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
		reservationForOccasionalVisitorController.setIdentFields(employee);
		mainPane.getChildren().clear();
		mainPane.getChildren().add(root);
	}

}