package client;
import javafx.application.Application;
import javafx.stage.Stage;
import gui.SearchGuiController;

 
/**
 * @author Dan Gutchin 
 * @author Yaniv Sokolov 
 * @author Rafael elkoby 
 * @version December 3 2020
 */
public class ClientUI extends Application {
	
	public static ClientController chat; //only one instance

	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   } // end main
	 
	/**
	* This method override the superclass method start and displays the GUI on to
	* the screen
	* 
	* @param primaryStage the primary stage for this application
	*/
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		chat= new ClientController("localhost", 5555);
		SearchGuiController aFrame = new SearchGuiController(); // create Search Controller
		
		aFrame.start(primaryStage);
	}
	
	
}
