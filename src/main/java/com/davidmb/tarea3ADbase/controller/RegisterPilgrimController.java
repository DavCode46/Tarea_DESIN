package com.davidmb.tarea3ADbase.controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.Carnet;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.PilgrimStops;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.PilgrimStopsService;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.utils.ManagePassword;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@Controller
public class RegisterPilgrimController implements Initializable {

	@FXML
	private TextField nameField;

	@FXML
	private TextField emailField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private TextField passwordVisibleField;

	@FXML
	private PasswordField confirmPasswordField;

	@FXML
	private TextField confirmPasswordVisibleField;

	@FXML
	private CheckBox showPasswordCheckBox;

	@FXML
	private ComboBox<String> nationalityComboBox;

	@FXML
	private ComboBox<Stop> stopComboBox;

	@FXML
	private Label errorLabel;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private UserService userService;

	@Autowired
	private PilgrimService pilgrimService;

	@Autowired
	private StopService stopService;

	@Autowired
	private PilgrimStopsService pilgrimStopsService;

	@FXML
	private void registerPilgrim() {
		if (validateData()) {

			String password = "";

			String name = nameField.getText();
			String email = emailField.getText();
			String nationality = nationalityComboBox.getValue();
			Stop stop = stopComboBox.getValue();
			if (passwordVisibleField.isVisible()) {
				password = passwordVisibleField.getText();
			} else {
				password = passwordField.getText();
			}

			Stop currentStop = stopService.find(stop.getId());

			Carnet carnet = new Carnet(currentStop);

			User user = new User(name, "Peregrino", email, password);
			Pilgrim pilgrim = new Pilgrim(name, nationality, carnet, null);
			if (confirmAlert(user, pilgrim)) {
				User newUser = userService.save(user);

				pilgrim.setUserId(newUser.getId());
				PilgrimStops initialPilgrimStop = new PilgrimStops(pilgrim, currentStop, LocalDate.now());

				currentStop.getPilgrimStops().add(initialPilgrimStop);

				pilgrim.getPilgrimStops().add(initialPilgrimStop);

				pilgrimService.save(pilgrim);
				pilgrimStopsService.save(initialPilgrimStop);

				showInfoAlert(user);
				clearFields();
				stageManager.switchScene(FxmlView.LOGIN);
			} else {
				showInfoAlert(null);
			}
		}
	}

	@FXML
	private void togglePasswordVisibility() {
		ManagePassword.showPassword(passwordVisibleField, passwordField, showPasswordCheckBox,
				confirmPasswordVisibleField, confirmPasswordField);
	}

	@FXML
	private void backToLogin() {
		// Cambiar a la vista de "Login"
		stageManager.switchScene(FxmlView.LOGIN);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadStops();
		loadNationalities();
	}

	/**
	 * Carga las nacionalidades desde el archivo XML al ComboBox.
	 */
	private void loadNationalities() {
		try {
			File file = new File("src/main/resources/paises.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);
			document.getDocumentElement().normalize();

			// Obtener todos los nodos <nombre> del XML
			NodeList countryNodes = document.getElementsByTagName("nombre");
			for (int i = 0; i < countryNodes.getLength(); i++) {
				String countryName = countryNodes.item(i).getTextContent();
				nationalityComboBox.getItems().add(countryName);
			}

		} catch (Exception e) {
			errorLabel.setText("Error al cargar las nacionalidades.");
			errorLabel.setStyle("-fx-text-fill: red;");
			e.printStackTrace();
		}
	}

	private void loadStops() {
		stopComboBox.getItems().clear();
		stopService.findAll().forEach(stop -> stopComboBox.getItems().add(stop));
	}

	private void showInfoAlert(User user) {
		Alert alert = new Alert(AlertType.INFORMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		if (user != null) {
			alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
			alert.setTitle("Registro completado");
			alert.setHeaderText("Registro completado");
			alert.setContentText("Sus datos son: \nUsuario: " + user.getEmail() + " registrado correctamente.");
		} else {
			alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/info.png")));
			alert.setTitle("Registro cancelado");
			alert.setHeaderText("Registro cancelado");
			alert.setContentText("Registro cancelado");
		}

		alert.showAndWait();
	}

	private boolean confirmAlert(User user, Pilgrim pilgrim) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirma tus datos");
		alert.setHeaderText("Confirma tus datos");
		alert.setContentText("Sus datos son: \nUsuario: " + user.getEmail() + "\n" + "Peregrino: " + pilgrim.getName()
				+ "\n" + "Nacionalidad: " + pilgrim.getNationality() + "\n" + "Parada: "
				+ pilgrim.getCarnet().getInitialStop().getName() + "\n" + "¿Son correctos tus datos?");
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/confirm.png")));
		alert.showAndWait();
		return alert.getResult().getButtonData().isDefaultButton();
	}

	private void showErrorAlert(StringBuilder message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al registrar peregrino");
		alert.setHeaderText("Error al registrar peregrino");
		alert.setContentText(message.toString());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}

	private boolean validateData() {
		boolean ret = false;
		StringBuilder message = new StringBuilder();
		String name = nameField.getText();
		String email = emailField.getText();
		String password = passwordField.getText();
		String passwordVisible = passwordVisibleField.getText();
		String confirmPassword = confirmPasswordField.getText();
		String confirmPasswordVisible = confirmPasswordVisibleField.getText();

		User emailExists = userService.findByEmail(email);

		// Validar nombre
		if (name.isEmpty()) {
			message.append("El nombre no puede estar vacío.\n");
		} else if (name.length() > 20) {
			message.append("El nombre no puede tener más de 20 caracteres.\n");
		} else if (name.chars().anyMatch(Character::isDigit)) {
			message.append("El nombre no puede contener números.\n");
		}

		// Validar Email
		if (email.isEmpty()) {
			message.append("El Email no puede estar vacío.\n");
		} else if (emailExists != null) {
			message.append("El Email ya está registrado.\n");
		} else if (email.length() > 50) {
			message.append("El Email no puede tener más de 50 caracteres.\n");
		} else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
			message.append("El Email no tiene un formato válido.\n");
		}

		// Validar Password
		if (passwordField.isVisible()) {
			if (password.isEmpty()) {
				message.append("La contraseña no puede estar vacía.\n");
			} else if (password.length() < 8) {
				message.append("La contraseña debe tener al menos 8 caracteres.\n");
			} else if (!password.matches(".*\\d.*")) {
				message.append("La contraseña debe contener al menos un número.\n");
			} else if (!password.matches(".*[a-z].*")) {
				message.append("La contraseña debe contener al menos una letra minúscula.\n");
			} else if (!password.matches(".*[A-Z].*")) {
				message.append("La contraseña debe contener al menos una letra mayúscula.\n");
			} else if (!password.matches(".*[!@#$%^&*].*")) {
				message.append("La contraseña debe contener al menos un carácter especial.\n");
			} else if (!password.equals(confirmPassword)) {
				message.append("Las contraseñas no coinciden.\n");
			}
		}else {
			if (passwordVisible.isEmpty()) {
				message.append("La contraseña no puede estar vacía.\n");
			} else if (passwordVisible.length() < 8) {
				message.append("La contraseña debe tener al menos 8 caracteres.\n");
			} else if (!passwordVisible.matches(".*\\d.*")) {
				message.append("La contraseña debe contener al menos un número.\n");
			} else if (!passwordVisible.matches(".*[a-z].*")) {
				message.append("La contraseña debe contener al menos una letra minúscula.\n");
			} else if (!passwordVisible.matches(".*[A-Z].*")) {
				message.append("La contraseña debe contener al menos una letra mayúscula.\n");
			} else if (!passwordVisible.matches(".*[!@#$%^&*].*")) {
				message.append("La contraseña debe contener al menos un carácter especial.\n");
			} else if (!passwordVisible.equals(confirmPasswordVisible)) {
				message.append("Las contraseñas no coinciden.\n");
			}
		}

		// Validar Nacionalidad
		if (nationalityComboBox.getValue() == null) {
			message.append("Debe seleccionar una nacionalidad.\n");
		}

		// Validar Parada
		if (stopComboBox.getValue() == null) {
			message.append("Debe seleccionar una parada.\n");
		}

		if (message.length() == 0) {
			ret = true;
		} else {
			showErrorAlert(message);
		}

		return ret;

	}

	private void clearFields() {
		nameField.clear();
		emailField.clear();
		passwordField.clear();
		confirmPasswordField.clear();
		passwordVisibleField.clear();
		nationalityComboBox.setValue(null);
		stopComboBox.setValue(null);
	}

}
