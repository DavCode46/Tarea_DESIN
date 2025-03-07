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
 * Controlador para la pantalla de inicio de sesión.
 * 
 * Permite a los usuarios autenticarse en la aplicación con su usuario y contraseña,
 * así como acceder a las opciones de registro y recuperación de contraseña.
 * 
 * Implementa `Initializable` para la configuración inicial de la interfaz gráfica.
 * 
 * @author DavidMB
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

	  /**
     * Maneja el proceso de inicio de sesión.
     * 
     * - Obtiene las credenciales del usuario.
     * - Autentica al usuario en el sistema.
     * - Redirige a la vista correspondiente según su rol.
     * - Muestra una alerta en caso de error.
     * 
     * @param event Evento de acción generado por el botón de inicio de sesión.
     * @throws IOException Si ocurre un error al cambiar de escena.
     */
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

	/**
     * Muestra la ventana de ayuda de la aplicación.
     */
	@FXML
	private void showHelp() {
		HelpUtil.showHelp();
	}

	  /**
     * Captura eventos de teclado para detectar la tecla F1 y mostrar la ayuda.
     * 
     * @param event Evento de teclado generado.
     */
	public void handleKeyPressed(KeyEvent event) {
		if (event.getCode().toString().equals("F1")) {
			// Llamar al mismo método showHelp() al presionar F1
			showHelp();
		}
	}

	/**
     * Muestra una alerta de error en caso de credenciales incorrectas.
     */
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

	 /**
     * Muestra una alerta de éxito al iniciar sesión correctamente.
     * 
     * @param user Usuario autenticado.
     */
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

	/**
     * Limpia los campos del formulario de inicio de sesión.
     */
	public void clearFields() {
		username.clear();
		password.clear();
		passwordVisibleField.clear();
	}

	  /**
     * Permite abrir la ventana de recuperación de contraseña.
     * 
     * @param event Evento de acción generado al hacer clic en "¿Olvidaste tu contraseña?".
     * @throws IOException Si ocurre un error al cambiar de escena.
     */
	@FXML
	public void openForgotPasswordView(ActionEvent event) throws IOException {
		// Cambiar a la vista de "Forgot Password"
		stageManager.switchScene(FxmlView.FORGOT_PASSWORD);
	}

	/**
     * Permite abrir la ventana de registro de peregrino.
     * 
     * @param event Evento de acción generado al hacer clic en "Registrarse como peregrino".
     * @throws IOException Si ocurre un error al cambiar de escena.
     */
	@FXML
	public void openRegisterPilgrimView(ActionEvent event) throws IOException {
		// Cambiar a la vista de "RegisterPilgrim"
		stageManager.switchScene(FxmlView.REGISTER_PILGRIM);
	}

	/**
     * Alterna la visibilidad de la contraseña entre un `PasswordField` y un `TextField`.
     */
	@FXML
	private void togglePasswordVisibility() {
		ManagePassword.showPassword(passwordVisibleField, password, showPasswordCheckBox, null, null);
	}

	/**
     * Obtiene la contraseña ingresada en el formulario.
     * 
     * @return Contraseña ingresada por el usuario.
     */
	public String getPassword() {
		return password.getText();
	}

	/**
     * Obtiene el nombre de usuario ingresado en el formulario.
     * 
     * @return Nombre de usuario ingresado.
     */
	public String getUsername() {
		return username.getText();
	}

	/**
     * Inicializa la interfaz gráfica y configura los tooltips para mejorar la usabilidad.
     * 
     * @param location URL de inicialización.
     * @param resources Recursos utilizados en la inicialización.
     */
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

	 /**
     * Obtiene el botón de inicio de sesión.
     * 
     * @return Botón de inicio de sesión.
     */
	public Button getBtnLogin() {
		return btnLogin;
	}

	/**
     * Establece el botón de inicio de sesión.
     * 
     * @param btnLogin Botón de inicio de sesión.
     */
	public void setBtnLogin(Button btnLogin) {
		this.btnLogin = btnLogin;
	}


    /**
     * Obtiene el botón de registro de peregrino.
     * 
     * @return Botón de registro de peregrino.
     */
	public Button getBtnRegisterPilgrim() {
		return btnRegisterPilgrim;
	}

	/**
     * Establece el botón de registro de peregrino.
     * 
     * @param btnRegisterPilgrim Botón de registro de peregrino.
     */
	public void setBtnRegisterPilgrim(Button btnRegisterPilgrim) {
		this.btnRegisterPilgrim = btnRegisterPilgrim;
	}

	 /**
     * Obtiene el botón de recuperación de contraseña.
     * 
     * @return Botón de recuperación de contraseña.
     */
	public Button getBtnForgotPassword() {
		return btnForgotPassword;
	}

	 /**
     * Establece el botón de recuperación de contraseña.
     * 
     * @param btnForgotPassword Botón de recuperación de contraseña.
     */
	public void setBtnForgotPassword(Button btnForgotPassword) {
		this.btnForgotPassword = btnForgotPassword;
	}

	/**
     * Obtiene el botón de ayuda.
     * 
     * @return Botón de ayuda.
     */
	public Button getBtnHelp() {
		return btnHelp;
	}

	/**
     * Establece el botón de ayuda.
     * 
     * @param btnHelp Botón de ayuda.
     */
	public void setBtnHelp(Button btnHelp) {
		this.btnHelp = btnHelp;
	}

}
