package managerApproveChanges;

import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
import client.ChatClient;
import client.ClientUI;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**
 * This class will provide all details about requests from park managers to department manager.
 * The department manager should approve/reject the requests, therefore the details will be updated in the DB.
 * 
 * @author Shay Maryuma
 * @version December 27 2020
 */


public class ApproveChangesController {
	Gson gson = new Gson();

	@FXML
	private Text exitBtn;

	@FXML
	private VBox listVBox;

	//Example for how the requests will be look like in our arraylist.
	//Banias,100,20,40,123,32,2,waiting,waiting,finished
	//Niagara,100,20,40,123,32,2,waiting,waiting,finished
	//Safari,100,20,40,123,32,2,waiting,finished,waiting

	private ArrayList<String[]> allChangesAsTexts = new ArrayList<>();

	//Fields are: Capacity, Difference, Discount
	private final int numberOfFields = 3;
	private int numberOfParks;

	//Accept and Reject Buttons, suppose to be 9 approve/reject buttons for 3 parks with 3 fields ( 3 x 3 = 9).
	Button[] aprBtns;
	Button[] rejBtns;
	
	/**
	 * Quit the current scene, stay in the main page for department manager.
	 */
	@FXML
	void quitScene(MouseEvent event) throws IOException {
		StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();	
	}
	
	
	//-------------------------------------------- Interact with server START-----------------------------------------------

	public void getChangesFromDataBase() {
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "showAllApprovesAndRejects", "");
		ClientUI.chat.accept(gson.toJson(rh));
		checkIfGetDataQueryWorked();
	}
	public void setStatusToRequest(String parkNameAndChangeField) {
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "updateFieldStatus", parkNameAndChangeField);
		ClientUI.chat.accept(gson.toJson(rh));
	}
	public void updateParkDetails(String parkNameAndChangeFieldAndNewData) {
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "updateParkInformation", parkNameAndChangeFieldAndNewData);
		ClientUI.chat.accept(gson.toJson(rh));
	}
	//-------------------------------------------- Interact with server END -----------------------------------------------
	
	//-------------------------------------------- Analyze answers from server START ------------------------------------------
	//analyzeAnswerFromServer
	private void checkIfGetDataQueryWorked() {
		String answer = ChatClient.serverMsg;		
		if(!answer.equals("faild"))
			loadAllRequests(answer);
	}		
	//-------------------------------------------- Analyze answers from server END ------------------------------------------

	// All the requests will be in a template String
	// This method will make sure to split it and use it as an ArrayList.
	private void setAllChangesAsTexts(String changes)
	{
		String[] tables = changes.split(" ");

		for (String s : tables) 
			allChangesAsTexts.add(s.split(","));

		numberOfParks = allChangesAsTexts.size();
		aprBtns = new Button[numberOfFields * numberOfParks];
		rejBtns = new Button[numberOfFields * numberOfParks];

	}

	//Actions for approve clicking on approve button
	public void approveRequest(String parkName,String newData,int dataIndex)
	{
		setNewStatusForRequest(parkName,newData,dataIndex);
		String stringToSend = parkName + " " + whatIsTheField(dataIndex) + " " + newData;
		updateParkDetails(stringToSend);
		PopUp.display("Updated Successfully", "Update Successfully");
		refreshPage();
		
	}
	//Actions for approve clicking on reject button
	public void rejectRequest(String parkName,int dataIndex)
	{
		String theField = whatIsTheField(dataIndex);
		setStatusToRequest(parkName + " " + theField);
		PopUp.display("Reject Request", "Request has been rejected");
		refreshPage();
	}
	
	//After clicking on approve/reject button we need to set the request status to "finished"
	//Other untouched requests will be defined as "waiting".
	public void setNewStatusForRequest(String parkName,String newData,int dataIndex)
	{
		String theField = whatIsTheField(dataIndex);
		setStatusToRequest(parkName + " " + theField);
	//	refreshPage();
	}
	
	//Give actions to the empty buttons.
	public void setApproveAndRejectButtonsActions()
	{
		int buttonsCounter = 0;
		for (int i = 0; i < numberOfParks; i++) 
			for(int j = 0; j < numberOfFields; j++)
			{
				String parkName = allChangesAsTexts.get(i)[0];
				String newData = allChangesAsTexts.get(i)[j+1];
				int dataIndex = j;
				rejBtns[buttonsCounter].setOnAction(e -> rejectRequest(parkName,dataIndex));
				aprBtns[buttonsCounter++].setOnAction(e -> approveRequest(parkName,newData,dataIndex));
			}		
	}
	


	// Determine which field are we using.
	public String whatIsTheField(int dataIndex)
	{
		switch(dataIndex)
		{
		case 0:
			return "capacity";
		case 1:
			return "difference";
		case 2:
			return "discount";
		}
		return null;
	}
	
	//Remove the current data from the page and reload it
	//The advantage of this function is that the page will be organized.
	private void refreshPage()
	{
		listVBox.getChildren().clear();
		allChangesAsTexts.clear();
		getChangesFromDataBase();
	}
	
	//Load all the requests.
	//Requests that have been finished won't be load to this page.
	private void loadAllRequests(String changes)
	{
		
		setAllChangesAsTexts(changes);
		
		Font commonFontSize12 = Font.font("System", FontWeight.BOLD, 12);
		Font commonFontSize17 = Font.font("System", FontWeight.NORMAL, 17);
		
		int buttonsCounter = 0; // count how many approve and reject buttons we have.
		
		for(int i = 0; i < numberOfParks; i++)
		{
			//VBox that will hold each park changes.
			VBox vb = new VBox();
			vb.setPrefWidth(613);
			vb.setPrefHeight(52);
			vb.setSpacing(5);
			
			//Park name HBox
			HBox hb1 = new HBox();
			hb1.setPrefWidth(180);
			hb1.setPrefHeight(25);
			
			String parkName = allChangesAsTexts.get(i)[0];
			Text parkNameText = new Text("Park " + parkName + ":");
			parkNameText.setFont(Font.font("System", FontWeight.BOLD, 17));
			hb1.getChildren().add(parkNameText);
			
			//3 HBox for 3 changes
			HBox hb[] = new HBox[3];
			for (int j = 0; j < numberOfFields; j++)
			{
				hb[j] = new HBox();
				hb[j].setPrefSize(180, 25);	
				hb[j].setSpacing(10);
			}
			
			String capacity = allChangesAsTexts.get(i)[1];
			String difference = allChangesAsTexts.get(i)[2];
			String discount = allChangesAsTexts.get(i)[3];
			
			String oldCapacity = allChangesAsTexts.get(i)[4];
			String oldDifference = allChangesAsTexts.get(i)[5];
			String oldDiscount = allChangesAsTexts.get(i)[6];
			
			String capStatus = allChangesAsTexts.get(i)[7];
			String difStatus = allChangesAsTexts.get(i)[8];
			String disStatus = allChangesAsTexts.get(i)[9];
						
			Text capText = new Text(String.format("Capacity   = %5s \t->\t New Capacity   = %5s \t", oldCapacity,capacity));
			Text difText = new Text(String.format("Difference = %5s \t->\t New Difference = %5s \t", oldDifference,difference));
			Text disText = new Text(String.format("Discount   = %5s \t->\t New Discount   = %5s \t", oldDiscount,discount));
								
			capText.setFont(commonFontSize17);
			difText.setFont(commonFontSize17);
			disText.setFont(commonFontSize17);
			
			//Create
			for (int j = i*numberOfFields; j < i*numberOfFields + numberOfFields; j++)							
				createButton(j, commonFontSize12);
						
			
			buttonsCounter = addWaitingRequestsToTable(buttonsCounter, vb, hb1, hb, capStatus, difStatus, disStatus, capText, difText,disText);
			
		}
		setApproveAndRejectButtonsActions();
		
	}

	private int addWaitingRequestsToTable(int buttonsCounter, VBox vb, HBox hb1, HBox[] hb, String capStatus, String difStatus,
			String disStatus, Text capText, Text difText, Text disText) {
		
		vb.getChildren().add(hb1); //Hbox with park name
		
		int countFinishedChanges = 0;
		if(capStatus.equals("waiting"))
		{
			hb[0].getChildren().addAll(capText,aprBtns[buttonsCounter],rejBtns[buttonsCounter]);
			vb.getChildren().add(hb[0]);
			countFinishedChanges++;
		}
		buttonsCounter++;		
		if(difStatus.equals("waiting"))
		{
			hb[1].getChildren().addAll(difText,aprBtns[buttonsCounter],rejBtns[buttonsCounter]);
			vb.getChildren().add(hb[1]);
			countFinishedChanges++;
		}
		buttonsCounter++;
		if(disStatus.equals("waiting"))
		{
			hb[2].getChildren().addAll(disText,aprBtns[buttonsCounter],rejBtns[buttonsCounter]);
			vb.getChildren().add(hb[2]);
			countFinishedChanges++;
		}
		buttonsCounter++;
		
		if(countFinishedChanges != 0)
			listVBox.getChildren().add(vb);
		return buttonsCounter;
	}

	public void createButton(int j,Font f)
	{
		aprBtns[j] = new Button("Approve");
		rejBtns[j] = new Button("Reject");
					
		aprBtns[j].setFont(f);
		aprBtns[j].setStyle("-fx-background-color: #2ECC71; ");
		aprBtns[j].setTextFill(Color.WHITE);
		aprBtns[j].setPrefSize(100, 23);
		aprBtns[j].setTranslateX(10);
		
		rejBtns[j].setFont(f);
		rejBtns[j].setStyle("-fx-background-color: #E74C3C; ");
		rejBtns[j].setTextFill(Color.WHITE);
		rejBtns[j].setPrefSize(100, 23);
		rejBtns[j].setTranslateX(10);
	}

}
