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

public class ApproveChangesController {
	Gson gson = new Gson();
	

    @FXML
    private Text exitBtn;

    @FXML
    private VBox listVBox;

    
	private ArrayList<String[]> allChangesAsTexts = new ArrayList<>();

	
	@FXML
	void quitScene(MouseEvent event) throws IOException {
		StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();	
	}
	
	public void getChangesFromDataBase() {
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "approveParkChanges", "NULL");
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}
	
	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;
		switch (answer) {
		case "faild":
			PopUp.display("Empty list", answer);
			break;
		
		default:
			setChangesInReport(answer);
			break;
		}
	}

	private void setChangesInReport(String changes)
	{
		String[] tables = changes.split(" ");
		
		for (String s : tables) 
			allChangesAsTexts.add(s.split(","));
		
		Font commonFontSize12 = Font.font("System", FontWeight.BOLD, 12);
		Font commonFontSize17 = Font.font("System", FontWeight.NORMAL, 17);
		
		for(int i = 0; i < allChangesAsTexts.size(); i++)
		{
			VBox vb = new VBox();
			vb.setPrefWidth(613);
			vb.setPrefHeight(52);
			
			
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
			for (int j = 0; j < 3; j++)
			{
				hb[j] = new HBox();
				hb[j].setPrefWidth(217);
				hb[j].setPrefHeight(25);	
				hb[j].setSpacing(10);
			}
			
			String beforeText = allChangesAsTexts.get(i)[1];
			String afterText = allChangesAsTexts.get(i)[2];
			
			Text capText = new Text("Capacity = " + beforeText + "\t->\t" + "New Capacity = "+ afterText + "\t\t");
			Text difText = new Text("Difference = " + beforeText + "\t->\t" + "New Difference = "+ afterText + "\t\t");
			Text disText = new Text("Discount = " + beforeText + "\t->\t" + "New Discount = "+ afterText + "\t\t");
			
			capText.setFont(commonFontSize17);
			difText.setFont(commonFontSize17);
			disText.setFont(commonFontSize17);
			
			//Accept and Reject Buttons
			Button[] aprBtns = new Button[3];
			Button[] rejBtns = new Button[3];
			
			for (int j = 0; j < 3; j++)
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
			
			hb[0].getChildren().add(capText);
			hb[1].getChildren().add(difText);
			hb[2].getChildren().add(disText);
			
			for (int j = 0; j < 3; j++)
			{	
				hb[j].getChildren().add(aprBtns[j]);
				hb[j].getChildren().add(rejBtns[j]);
			}
			vb.getChildren().add(hb1);
			for (int j = 0; j < 3; j++)
			{
				vb.getChildren().add(hb[j]);	
			}

			listVBox.getChildren().add(vb);
		
		
		}
	}
	
}
