package popup;



import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUp {

	public static void display(String Titel,String msg) {
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(Titel);
		window.setMinWidth(250);
		window.setMinHeight(175);
		Label labale = new Label();
		labale.setText(msg);
		
		Button OkBtn = new Button();
		OkBtn.setOnAction(e->{
			window.close();
		});
		OkBtn.setText("OK");
		
		VBox layout = new VBox();
		layout.getChildren().addAll(labale,OkBtn);
		layout.setAlignment(Pos.CENTER);
		Insets pad = new Insets(15,15,15,15);
		layout.setPadding(pad);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}
