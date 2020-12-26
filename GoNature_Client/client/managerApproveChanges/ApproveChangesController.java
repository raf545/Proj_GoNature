package managerApproveChanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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

public class ApproveChangesController {
	Gson gson = new Gson();

	@FXML
	private Text exitBtn;

	@FXML
	private VBox listVBox;

	//Banias,100,20,40,123,32,2,waiting,waiting,finished
	//Niagara,100,20,40,123,32,2,waiting,waiting,finished
	//Safari,100,20,40,123,32,2,waiting,finished,waiting

	private ArrayList<String[]> allChangesAsTexts = new ArrayList<>();

	//Fields: Capacity, Difference, Discount
	private final int numberOfFields = 3;
	private int numberOfParks;

	//Accept and Reject Buttons, suppose to be 9 for 3 parks with 3 fields.
	Button[] aprBtns;
	Button[] rejBtns;

	@FXML
	void quitScene(MouseEvent event) throws IOException {
		StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();	
	}

	public void getChangesFromDataBase() {
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "showAllApprovesAndRejects", "");
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}
//	public void setChangesToDataBase(String parkNameAndChangeField) {
//		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "updateNewChangesToDB", parkNameAndChangeField);
//		ClientUI.chat.accept(gson.toJson(rh));
//		analyzeAnswerFromServer2();
//	}

	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;
		switch (answer) {
		case "faild":
			break;
		default:
			showAllApprovesAndRejects(answer);
		}
	}
	private void analyzeAnswerFromServer2() {
//		String answer = ChatClient.serverMsg;
//		switch (answer) {
//		case "faild":
//			break;
//		default:
//			updateNewChangesToDB();
//
//		}
	}

	private void setAllChangesAsTexts(String changes)
	{
		String[] tables = changes.split(" ");

		for (String s : tables) 
			allChangesAsTexts.add(s.split(","));

		numberOfParks = allChangesAsTexts.size();
		aprBtns = new Button[numberOfFields * numberOfParks];
		rejBtns = new Button[numberOfFields * numberOfParks];

	}
	private void showAllApprovesAndRejects(String changes)
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
			hb1.setPrefWidth(217);
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
				hb[j].setPrefWidth(217);
				hb[j].setPrefHeight(25);	
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


			Text capText = new Text("Capacity = " + oldCapacity + "\t->\t" + "New Capacity = "+ capacity + "\t\t");
			Text difText = new Text("Difference = " + oldDifference + "\t->\t" + "New Difference = "+ difference + "\t\t");
			Text disText = new Text("Discount = " + oldDiscount + "\t->\t" + "New Discount = "+ discount + "\t\t");

			capText.setFont(commonFontSize17);
			difText.setFont(commonFontSize17);
			disText.setFont(commonFontSize17);


			for (int j = i*numberOfFields; j < i*numberOfFields + numberOfFields; j++)
			{							
				aprBtns[j] = new Button("Approve");
				rejBtns[j] = new Button("Reject");

				aprBtns[j].setFont(commonFontSize12);
				aprBtns[j].setStyle("-fx-background-color: #2ECC71; ");
				aprBtns[j].setTextFill(Color.WHITE);
				aprBtns[j].setPrefWidth(131);
				aprBtns[j].setPrefHeight(23);

				rejBtns[j].setFont(commonFontSize12);
				rejBtns[j].setStyle("-fx-background-color: #E74C3C; ");
				rejBtns[j].setTextFill(Color.WHITE);
				rejBtns[j].setPrefWidth(131);
				rejBtns[j].setPrefHeight(23);
			}


			vb.getChildren().add(hb1); //Hbox with park name

			int countFinishedChanges = 0;
			if(capStatus.equals("waiting"))
			{
				hb[0].getChildren().add(capText);
				hb[0].getChildren().add(aprBtns[buttonsCounter]);
				hb[0].getChildren().add(rejBtns[buttonsCounter]);
				vb.getChildren().add(hb[0]);
				countFinishedChanges++;
			}
			buttonsCounter++;		
			if(difStatus.equals("waiting"))
			{
				hb[1].getChildren().add(difText);
				hb[1].getChildren().add(aprBtns[buttonsCounter]);
				hb[1].getChildren().add(rejBtns[buttonsCounter]);
				vb.getChildren().add(hb[1]);
				countFinishedChanges++;
			}
			buttonsCounter++;
			if(disStatus.equals("waiting"))
			{
				hb[2].getChildren().add(disText);
				hb[2].getChildren().add(aprBtns[buttonsCounter]);
				hb[2].getChildren().add(rejBtns[buttonsCounter]);
				vb.getChildren().add(hb[2]);
				countFinishedChanges++;
			}
			buttonsCounter++;

			if(countFinishedChanges != 0)
				listVBox.getChildren().add(vb);

		}
		setApproveAndRejectButtonsActions();

	}

	public void setApproveAndRejectButtonsActions()
	{
		int buttonsCounter = 0;
		for (int i = 0; i < numberOfParks; i++) 
			for(int j = 0; j < numberOfFields; j++)
			{
				String parkName = allChangesAsTexts.get(i)[0];
				String newData = allChangesAsTexts.get(i)[j+1];
				int dataIndex = j;
				rejBtns[buttonsCounter].setOnAction(e -> removeRequest(parkName,dataIndex));
				aprBtns[buttonsCounter++].setOnAction(e -> setNewDetailsInDB(parkName,newData,dataIndex));
			}		
	}
	public void setNewDetailsInDB(String parkName,String newData,int dataIndex)
	{
//		setChangesToDataBase("banias capacity");
//		analyzeAnswerFromServer2();
	}
	public void updateNewChangesToDB()
	{
//		System.out.println("Updated");

	}
	public void removeRequest(String parkName,int dataIndex)
	{
		System.out.println(whatIsTheField(dataIndex));
	}

	public String whatIsTheField(int dataIndex)
	{
		switch(dataIndex)
		{
		case 0:
			return "capacity";
		case 1:
			return "different";
		case 2:
			return "discount";
		}
		return null;
	}
}
