package com.davidmb.tarea3ADbase.controller;

import java.io.File;
import java.net.URL;
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
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


@Controller
public class RegisterPilgrimController implements Initializable {

	@FXML
	private TextField nameField;

	@FXML
	private TextField emailField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private PasswordField confirmPasswordField;

	@FXML
	private ComboBox<String> nationalityComboBox;
	
	@FXML
	private ComboBox<String> stopComboBox;

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

	@FXML
	private void registerPilgrim() {
		if(validateData()) {
			String name = nameField.getText();
			String email = emailField.getText();
			String password = passwordField.getText();
			String nationality = nationalityComboBox.getValue();
			String stop = stopComboBox.getValue();
			Stop currentStop = stopService.findByName(stop);
			Carnet carnet = new Carnet(currentStop);
			//carnetService.save(carnet);
			// Segundo username sería email
			User user = new User(name, "Peregrino", email, password);
			User newUser = userService.save(user);
			Pilgrim pilgrim = new Pilgrim(name, nationality, carnet, newUser.getId());
			pilgrimService.save(pilgrim);
			showInfoAlert(user);
			stageManager.switchScene(FxmlView.LOGIN);
		} else {
			errorLabel.setText("Error al registrar.");
			errorLabel.setStyle("-fx-text-fill: red;");
		}
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
		stopService.findAll().forEach(stop -> stopComboBox.getItems().add(stop.getName()));
	}
	
	private void showInfoAlert(User user) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Registro completado");
		alert.setHeaderText("Registro completado");
		alert.setContentText("Usuario " + user.getEmail() + " registrado correctamente.");
		alert.showAndWait();
	}
	
	private void showErrorAlert(StringBuilder message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al registrar");
		alert.setHeaderText("Error al registrar");
		alert.setContentText(message.toString());
		alert.showAndWait();
	}
	
	private boolean validateData() {
		boolean ret = false;
		StringBuilder message = new StringBuilder();
		String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        User emailExists = userService.findByEmail(email);
        
        // Validar nombre
        if(name.isEmpty()) {
        	message.append("El nombre no puede estar vacío.\n");
        } else if(name.length() > 20) {
        	message.append("El nombre no puede tener más de 20 caracteres.\n");
		} else if (name.chars().anyMatch(Character::isDigit)) {
			message.append("El nombre no puede contener números.\n");
		}
        
        // Validar Email
		if (email.isEmpty()) {
			message.append("El Email no puede estar vacío.\n");
		} else if (emailExists != null) {
			message.append("El Email ya está registrado.\n");
		} 
		else if (email.length() > 50) {
			message.append("El Email no puede tener más de 50 caracteres.\n");
		} else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            message.append("El Email no tiene un formato válido.\n");
		}
		
		// Validar Password
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
		
		// Validar Nacionalidad
		if (nationalityComboBox.getValue() == null) {
			message.append("Debe seleccionar una nacionalidad.\n");
		}
		
		// Validar Parada
		if (stopComboBox.getValue() == null) {
			message.append("Debe seleccionar una parada.\n");
		}
		
		if(message.length() == 0) {
			ret = true;
		} else {
			showErrorAlert(message);
		}
		
        return ret;
        
	}
	
	
}
