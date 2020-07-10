package application;
	
import java.io.IOException;

import gui.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.services.UserService;


public class Main extends Application {
	
private static Scene scene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader  = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			AnchorPane anchorpane = loader.load();
			scene = new Scene(anchorpane);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Login Page");
			primaryStage.show();
			
			MainViewController controller = loader.getController();
			controller.setUserService(new UserService());
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getScene() {
		return scene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
