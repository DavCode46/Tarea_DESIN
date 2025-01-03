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
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


@Controller
public class RegisterPilgrimController implements Initializable {

	@FXML
	private TextField nameField;

	@FXML
	private TextField usernameField;

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
	private StopService stopService;

	@FXML
	private void registerPilgrim() {
		String name = nameField.getText();
		String username = usernameField.getText();
		String password = passwordField.getText();
		String confirmPassword = confirmPasswordField.getText();
		String nationality = nationalityComboBox.getValue();
		Carnet carnet = new Carnet();
		User user = new User();
		Pilgrim pilgrim = new Pilgrim();
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
}
