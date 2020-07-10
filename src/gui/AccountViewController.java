package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import model.dao.DaoFactory;
import model.dao.UserDao;
import model.entities.User;
import model.services.UserService;

public class AccountViewController implements Initializable {

	Connection conn = null;

	private UserService service;

	private User user = new User();

	@FXML
	private TextField txtUser;

	@FXML
	private PasswordField txtPass;

	@FXML
	private TextField txtPass1;

	@FXML
	private TextField txtEmail;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnSair;

	@FXML
	private Button btnApagarConta;

	@FXML
	private Button btnValidar;

	@FXML
	private Button btnCancelar;

	@FXML
	private ToggleButton tbVer;

	@FXML
	public void onbtnEditarAction() {
		txtUser.setEditable(true);
		txtPass.setEditable(true);
		txtEmail.setEditable(true);
		tbVer.setVisible(true);
		btnValidar.setVisible(true);
		btnCancelar.setVisible(true);
		txtPass.clear();
		txtPass1.clear();
	}

	@FXML
	public void onbtnSairAction() {
		loadView("/gui/MainView.fxml", x -> {});
	}

	@FXML
	public void onbtnApagarContaAction() {
		Optional<ButtonType> apagar = Alerts.showConfirmation("APAGAR", "Tem a certeza que quer apagar a sua conta?");
		if (apagar.get() == ButtonType.OK) {
			if (service.delete(user)) {
				Alerts.showAlert("Conta Apagada", null, "A sua conta foi apagada com sucesso!", AlertType.INFORMATION);
				loadView("/gui/MainView.fxml", x -> {});
			} else {
				Alerts.showAlert("ERRO", null, "Houve um erro ao apagar a sua conta.\nContacte o administrador da aplicação.", AlertType.ERROR);
			}
		}
	}

	@FXML
	public void ontxtPassKeyTyped() {
		txtPass1.setText(txtPass.getText());
	}

	@FXML
	public void ontxtPass1KeyTyped() {
		txtPass.setText(txtPass1.getText());
	}

	@FXML
	public void ontbVerAction() {
		if (tbVer.isSelected()) {
			txtPass1.setVisible(true);
			txtPass1.setEditable(true);
		} else {
			txtPass1.setVisible(false);
		}
	}

	@FXML
	public void onbtnValidarAction() {
		UserDao dao = DaoFactory.createUserDao();
		User newuser = new User();
		newuser.setUsername(txtUser.getText());
		newuser.setEmail_adress(txtEmail.getText());
		newuser.setPassword(user.getPassword());
		if (!(txtPass.getText().isBlank())) {
			newuser.setPassword(txtPass.getText());
		}
		Optional<ButtonType> confirma = Alerts.showConfirmation("EDITAR", "Confirmar a edição dos seus dados?");
		if (confirma.get() == ButtonType.OK) {
			if (dao.findByUsername(newuser.getUsername()) != null && !(newuser.getUsername().equals(user.getUsername()))) {
				Alerts.showAlert("ERRO", null, "Este Username já existe. Tente Novamente!", AlertType.ERROR);
			} else if (dao.findByEmailAdress(newuser.getEmail_adress()) != null && !(newuser.getEmail_adress().equals(user.getEmail_adress()))) {
				Alerts.showAlert("ERRO", null, "Já existe uma conta associada a este Email. Tente Novamente!",
						AlertType.ERROR);
			} else if (!(txtPass.getText().isBlank()) && !(isValid(txtPass.getText()))) {
				Alerts.showAlert("ERROR", null, "A password tem de conter:\n"
						+ "Pelo menos 8 caracteres até um máximo de 16;\n"
						+ "Pelo menos 1 Letra Maiúscula;\nPelo menos 1 Letra Minúscula;\n"
						+ "Pelo menos 1 Número;\nPelo menos 1 Caractere Especial.", AlertType.ERROR);
			} else {
				if (service.update(user, newuser)) {
					user.setUsername(newuser.getUsername());
					user.setPassword(newuser.getPassword());
					user.setEmail_adress(newuser.getEmail_adress());
					Alerts.showAlert("SUCESSO", null, "Os seus dados foram editados com sucesso!", AlertType.INFORMATION);
					txtUser.setEditable(false);
					txtPass.setEditable(false);
					txtEmail.setEditable(false);
					tbVer.setVisible(false);
					btnValidar.setVisible(false);
					btnCancelar.setVisible(false);
					txtPass.setText("********");
					if (tbVer.isSelected()) {
						txtPass1.setVisible(false);
						tbVer.setSelected(false);
					}
				} else {
					Alerts.showAlert("ERRO", null, "Algo correu mal! Contacte o Administrador da aplicação!", AlertType.ERROR);
				}
			}
			
		}
		
		
	}

	@FXML
	public void onbtnCancelarAction() {
		txtUser.setEditable(false);
		txtPass.setEditable(false);
		txtEmail.setEditable(false);
		tbVer.setVisible(false);
		btnValidar.setVisible(false);
		btnCancelar.setVisible(false);
		txtPass.setText("********");
		txtUser.setText(user.getUsername());
		txtEmail.setText(user.getEmail_adress());
		if (tbVer.isSelected()) {
			txtPass1.setVisible(false);
			tbVer.setSelected(false);
		}
	}

	public void setUserService(UserService service) {
		this.service = service;
	}
	
	public void setUser(String username, String password, String email_adress) {
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail_adress(email_adress);
		txtUser.setText(username);
		txtEmail.setText(email_adress);
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
		
	}
}
