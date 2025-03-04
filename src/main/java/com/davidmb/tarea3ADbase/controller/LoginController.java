package com.davidmb.tarea3ADbase.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.utils.HelpUtil;
import com.davidmb.tarea3ADbase.utils.ManagePassword;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**

 */

@Controller
public class LoginController implements Initializable {

	@FXML
	public Button btnLogin;

	@FXML
	public Button btnRegisterPilgrim;

	@FXML
	public Button btnForgotPassword;

	@FXML
	public Button btnHelp;

	@FXML
	public PasswordField password;

	@FXML
	public TextField username;

	@FXML
	public CheckBox showPasswordCheckBox;

	@FXML
	public TextField passwordVisibleField;

	@FXML
	public Label lblLogin;

	@Autowired
	private UserService userService;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private Session session;

	@FXML
	public void login(ActionEvent event) throws IOException {
		String password = "";
		if (passwordVisibleField.isVisible()) {
			password = passwordVisibleField.getText();
		} else {
			password = this.password.getText();
		}
		if (userService.authenticate(getUsername(), password)) {

			User user = userService.findByEmail(getUsername());
			session.setLoggedInUser(user);
			showSuccessAlert(user);

			switch (user.getRole().toUpperCase()) {
			case "ADMIN" -> stageManager.switchScene(FxmlView.ADMIN);
			case "PEREGRINO" -> stageManager.switchScene(FxmlView.PILGRIM);
			case "PARADA" -> stageManager.switchScene(FxmlView.STOP);
			}

			clearFields();

		} else {
			showErrorAlert();
		}
	}

	@FXML
	private void showHelp() {
		HelpUtil.showHelp();
	}

	public void handleKeyPressed(KeyEvent event) {
		if (event.getCode().toString().equals("F1")) {
			// Llamar al mismo método showHelp() al presionar F1
			showHelp();
		}
	}

	public void showErrorAlert() {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al iniciar sesión");
		alert.setHeaderText("Usuario o contraseña incorrectos");
		alert.setContentText("Por favor, introduzca un usuario y contraseña válidos");
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();

	}

	public void showSuccessAlert(User user) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Sesión iniciada como: " + user.getEmail());
		alert.setHeaderText(null);
		alert.setContentText("¡Bienvenido: " + user.getEmail() + "!\n");
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
		alert.showAndWait();

	}

	public void clearFields() {
		username.clear();
		password.clear();
		passwordVisibleField.clear();
	}

	@FXML
	public void openForgotPasswordView(ActionEvent event) throws IOException {
		// Cambiar a la vista de "Forgot Password"
		stageManager.switchScene(FxmlView.FORGOT_PASSWORD);
	}

	@FXML
	public void openRegisterPilgrimView(ActionEvent event) throws IOException {
		// Cambiar a la vista de "RegisterPilgrim"
		stageManager.switchScene(FxmlView.REGISTER_PILGRIM);
	}

	@FXML
	private void togglePasswordVisibility() {
		ManagePassword.showPassword(passwordVisibleField, password, showPasswordCheckBox, null, null);
	}

	public String getPassword() {
		return password.getText();
	}

	public String getUsername() {
		return username.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getBtnHelp().setTooltip(new Tooltip("Pulsa F1 para obtener ayuda"));
		getBtnLogin().setTooltip(new Tooltip("Iniciar sesión"));
		getBtnRegisterPilgrim().setTooltip(new Tooltip("Registrarse como peregrino"));
		getBtnForgotPassword().setTooltip(new Tooltip("Recuperar contraseña"));
		showPasswordCheckBox.setTooltip(new Tooltip("Mostrar contraseña"));
		username.setTooltip(new Tooltip("Su Usuario"));
		password.setTooltip(new Tooltip("Su contraseña"));
	}

	public Button getBtnLogin() {
		return btnLogin;
	}

	public void setBtnLogin(Button btnLogin) {
		this.btnLogin = btnLogin;
	}

	public Button getBtnRegisterPilgrim() {
		return btnRegisterPilgrim;
	}

	public void setBtnRegisterPilgrim(Button btnRegisterPilgrim) {
		this.btnRegisterPilgrim = btnRegisterPilgrim;
	}

	public Button getBtnForgotPassword() {
		return btnForgotPassword;
	}

	public void setBtnForgotPassword(Button btnForgotPassword) {
		this.btnForgotPassword = btnForgotPassword;
	}

	public Button getBtnHelp() {
		return btnHelp;
	}

	public void setBtnHelp(Button btnHelp) {
		this.btnHelp = btnHelp;
	}

}
