package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import model.entities.User;
import model.services.UserService;

public class MainViewController implements Initializable {
	
	Connection conn = null;
	
	private UserService service;
	
	private User user = new User();
	
	@FXML
	private TextField txtUser;
	
	@FXML
	private PasswordField txtPass;
	
	@FXML
	private Button btnLogin;
	
	@FXML
	private Button btnRegistar;
	
	@FXML
	private TextField txtRepPass;
	
	@FXML
	private ToggleButton tbVer;
	
	@FXML
	public void onbtnLoginAction() {
		if (txtUser.getText().equals("") || txtPass.getText().equals("")) {
			Alerts.showAlert("ERRO", null, "Por favor, insira todos os dados!", AlertType.ERROR);
		} else {
			user.setUsername(txtUser.getText());
			user.setPassword(txtPass.getText());
			if (service.login(user)) {
				User useraccount = service.getUserData(user);
				Alerts.showAlert("SUCESSO", null, "Login bem efetuado!", AlertType.INFORMATION);
				loadAccountView("/gui/AccountView.fxml", (AccountViewController controller) -> {
					controller.setUser(useraccount.getUsername(), useraccount.getPassword(), useraccount.getEmail_adress());
				});
			} else {
				Alerts.showAlert("ERRO", null, "Username ou Password incorreto. Tente novamente ou clique em "
						+ "Registar!", AlertType.ERROR);
			}
		}
		
	}
	
	@FXML
	public void onbtnRegistarAction() {
		loadView("/gui/RegisterView.fxml", x -> {});
	}
	
	public void setUserService(UserService service) {
		this.service = service;
	}
	
	@FXML
	public void ontbVerAction() {
		if (tbVer.isSelected()) {
			txtRepPass.setVisible(true);
		} else {
			txtRepPass.setVisible(false);
		}
	}
	
	@FXML
	public void ontxtPassKeyTyped() {
		txtRepPass.setText(txtPass.getText());
	}
	
	@FXML
	public void ontxtRepPassKeyTyped() {
		txtPass.setText(txtRepPass.getText());
	}

	public synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			AnchorPane newAnchorPane = loader.load();
			Scene mainScene = Main.getScene();
			AnchorPane mainAnchorPane = (AnchorPane) mainScene.getRoot();
			mainAnchorPane.getChildren().clear();
			mainAnchorPane.getChildren().add(newAnchorPane);
			T controller = loader.getController();
			((RegisterViewController) controller).setUserService(new UserService());
			initializingAction.accept(controller);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public synchronized <T> void loadAccountView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			AnchorPane newAnchorPane = loader.load();
			Scene mainScene = Main.getScene();
			AnchorPane mainAnchorPane = (AnchorPane) mainScene.getRoot();
			mainAnchorPane.getChildren().clear();
			mainAnchorPane.getChildren().add(newAnchorPane);
			T controller = loader.getController();
			((AccountViewController) controller).setUserService(new UserService());
			initializingAction.accept(controller);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldMaxLenght(txtUser, 20);
		Constraints.setTextFieldMaxLenght(txtPass, 30);
	}
	
}
