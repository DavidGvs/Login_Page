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
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import model.dao.DaoFactory;
import model.dao.UserDao;
import model.entities.User;
import model.services.UserService;

public class RegisterViewController implements Initializable {

	Connection conn = null;

	private UserService service;

	private User user = new User();

	@FXML
	private TextField txtUser;

	@FXML
	private PasswordField txtPass1;

	@FXML
	private PasswordField txtPass2;

	@FXML
	private TextField txtMail;

	@FXML
	private TextField txtRepPass1;

	@FXML
	private TextField txtRepPass2;

	@FXML
	private Button btnVoltar;

	@FXML
	private Button btnRegistar;

	@FXML
	private ToggleButton tbVer1;

	@FXML
	private ToggleButton tbVer2;

	@FXML
	public void onbtnVoltarAction() {
		loadView("/gui/MainView.fxml", x -> {
		});
	}

	@FXML
	public void onbtnRegistarAction() {
		user.setUsername(txtUser.getText());
		user.setPassword(txtPass1.getText());
		user.setEmail_adress(txtMail.getText());
		UserDao dao = DaoFactory.createUserDao();
		if (txtUser.getText().equals("") || txtPass1.getText().equals("") || txtMail.getText().equals("")) {
			Alerts.showAlert("ERRO", null, "Por favor, insira todos os dados!", AlertType.ERROR);
		} else {
			if (dao.findByUsername(user.getUsername()) != null) {
				Alerts.showAlert("ERRO", null, "Este Username já existe. Tente Novamente!", AlertType.ERROR);
			} else if (dao.findByEmailAdress(user.getEmail_adress()) != null) {
				Alerts.showAlert("ERRO", null, "Já existe uma conta associada a este Email. Tente Novamente!",
						AlertType.ERROR);
			} else {
				if (isValid(txtPass1.getText())) {
					if (!(txtPass1.getText().equals(txtPass2.getText()))) {
						Alerts.showAlert("ERRO", null, "As passwords não coincidem. Tente Novamente!", AlertType.ERROR);
					} else {
						if (service.save(user)) {
							Alerts.showAlert("Registar", null, "Utilizador Registado com Sucesso!", AlertType.INFORMATION);
							loadView("/gui/MainView.fxml", x -> {
							});
						} else {
							Alerts.showAlert("Erro", null, "Alguma coisa correu mal...", AlertType.ERROR);
						}
					}
				} else Alerts.showAlert("ERROR", null, "A password tem de conter:\n"
						+ "Pelo menos 8 caracteres até um máximo de 16;\n"
						+ "Pelo menos 1 Letra Maiúscula;\nPelo menos 1 Letra Minúscula;\n"
						+ "Pelo menos 1 Número;\nPelo menos 1 Caractere Especial.", AlertType.ERROR);
			}
		}

	}

	@FXML
	public void ontbVer1Action() {
		if (tbVer1.isSelected()) {
			txtRepPass1.setVisible(true);
		} else {
			txtRepPass1.setVisible(false);
		}
	}

	@FXML
	public void ontbVer2Action() {
		if (tbVer2.isSelected()) {
			txtRepPass2.setVisible(true);
		} else {
			txtRepPass2.setVisible(false);
		}
	}

	@FXML
	public void ontxtPass1KeyTyped() {
		txtRepPass1.setText(txtPass1.getText());
	}

	@FXML
	public void ontxtPass2KeyTyped() {
		txtRepPass2.setText(txtPass2.getText());
	}

	@FXML
	public void ontxtRepPass1KeyTyped() {
		txtPass1.setText(txtRepPass1.getText());
	}

	@FXML
	public void ontxtRepPass2KeyTyped() {
		txtPass2.setText(txtRepPass2.getText());
	}

	public void setUserService(UserService service) {
		this.service = service;
	}
	
	public static boolean isValid(String password) {
		if (password.length() < 8) {
			return false;
		} else {
			return (isUpperLetter(password) && isLowerLetter(password) && isNumeric(password) && isSpecial(password));
		}
	}

	private static boolean isUpperLetter(String pass) {
		String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int up = 0;
		for (int i = 0; i < pass.length(); i++) {
			if (upper.contains(Character.toString(pass.charAt(i)))) {
				up++;
			}
		}
		return (up > 0);
	}

	private static boolean isLowerLetter(String pass) {
		String lower = "abcdefghijklmnopqrstuvwxyz";
		int low = 0;
		for (int i = 0; i < pass.length(); i++) {
			if (lower.contains(Character.toString(pass.charAt(i)))) {
				low++;
			}
		}
		return (low > 0);
	}

	private static boolean isNumeric(String pass) {
		String number = "0123456789";
		int num = 0;
		for (int i = 0; i < pass.length(); i++) {
			if (number.contains(Character.toString(pass.charAt(i)))) {
				num++;
			}
		}
		return (num > 0);
	}

	private static boolean isSpecial(String pass) {
		String special1 = "|!@#£$§%&/{([)]=}?'«»€*-+.´`^~-_:,;<>";
		int spec = 0;
		for (int i = 0; i < pass.length(); i++) {
			if (special1.contains(Character.toString(pass.charAt(i)))) {
				spec++;
			}
		}
		return (spec > 0);
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
			((MainViewController) controller).setUserService(new UserService());
			initializingAction.accept(controller);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldMaxLenght(txtUser, 20);
		Constraints.setTextFieldMaxLenght(txtPass1, 16);
		Constraints.setTextFieldMaxLenght(txtPass2, 16);
		Constraints.setTextFieldMaxLenght(txtRepPass1, 16);
		Constraints.setTextFieldMaxLenght(txtRepPass2, 16);
		Constraints.setTextFieldMaxLenght(txtMail, 45);
	}

}
