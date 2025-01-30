package com.davidmb.tarea3ADbase.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.utils.ManagePassword;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 
 */

@Controller
public class AdminController implements Initializable {

	@FXML
	private Button btnLogout;

	@FXML
	private Label loggedInUser;

	@FXML
	private TextField stopName;

	@FXML
	private ComboBox<String> cbregion;

	@FXML
	private TextField managerName;

	@FXML
	private TextField managerEmail;

	@FXML
	private PasswordField managerPassword;

	@FXML
	private TextField managerPasswordVisibleField;

	@FXML
	private PasswordField confirmManagerPassword;

	@FXML
	private TextField confirmManagerPasswordVisibleField;

	@FXML
	private CheckBox showPasswordCheckBox;

	@FXML
	private Button reset;

	@FXML
	private Button saveStop;

	@FXML
	private TableView<Stop> stopTable;

	@FXML
	private TableColumn<Stop, Long> colStopId;

	@FXML
	private TableColumn<Stop, String> colStopName;

	@FXML
	private TableColumn<Stop, String> colStopRegion;

	@FXML
	private TableColumn<Stop, String> colManagerEmail;

	@FXML
	private TableColumn<Stop, String> colManagerId;

	@FXML
	private MenuItem deleteStops;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private Session session;

	@Autowired
	private StopService stopService;
	@Autowired
	private UserService userService;

	private ObservableList<Stop> stopList = FXCollections.observableArrayList();
	private ObservableList<String> regions = FXCollections.observableArrayList();

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}

	/**
	 * Logout and go to the login page
	 */
	@FXML
	private void logout(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("¿Estás seguro que quieres cerrar sesión?");
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/logout.png")));
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			stageManager.switchScene(FxmlView.LOGIN);
		}
	}

	@FXML
	void reset(ActionEvent event) {
		clearFields();
	}

	@FXML
	private void saveStop(ActionEvent event) {

		if (validateData()) {
			String password = "";
			if (managerPasswordVisibleField.isVisible()) {
				password = managerPasswordVisibleField.getText();
			} else {
				password = managerPassword.getText();
			}
			Stop stop = new Stop(getStopName(), getRegion().substring(0, 3), getManagerName());
			User user = new User();
			user.setUsername(getManagerName());
			user.setEmail(getManagerEmail());
			user.setPassword(password);
			user.setRole("Parada");
			if(showConfirmAlert(user, stop)) {
				User newUser = userService.save(user);
				stop.setManager(newUser.getUsername());
				stop.setUserId(newUser.getId());

				boolean exists = stopService.existsByNameAndRegion(stop.getName(), stop.getRegion());

				if (!exists) {
					Stop newStop = stopService.save(stop);

					saveAlert(newStop);

					clearFields();
					loadStopDetails();
				} else {
					showErrorAlert(new StringBuilder("La parada ya existe."));
				}
			} else {
				saveAlert(null);
			}
		}
	}

	@FXML
	private void togglePasswordVisibility() {
		ManagePassword.showPassword(managerPasswordVisibleField, managerPassword, showPasswordCheckBox,
				confirmManagerPasswordVisibleField, confirmManagerPassword);
	}

	private void clearFields() {
		stopName.clear();
		managerName.clear();
		cbregion.setValue(null);
		managerEmail.clear();
		managerPassword.clear();
		confirmManagerPassword.clear();
	}

	private void saveAlert(Stop stop) {

		Alert alert = new Alert(AlertType.INFORMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		if(stop != null) {
			alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
			alert.setTitle("Parada registrada con éxito.");
			alert.setHeaderText("Parada registrada con éxito.");
		alert.setContentText(
				"La parada " + stop.getName() + " " + stop.getRegion() + " ha sido creada \nEl responsable es \n"
						+ getManagerEmail() + " con id " + +userService.find(stop.getUserId()).getId() + ".");
		} else {
			alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/info.png")));
			alert.setTitle("Registro cancelado");
            alert.setHeaderText("Registro cancelado");
            alert.setContentText("Registro de la parada cancelado, vuelve a introducir los datos");
		}
		// Cambiar el ícono de la ventana
		
		
		alert.showAndWait();
	}

	private void showErrorAlert(StringBuilder message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al registrar parada");
		alert.setHeaderText("Error al registrar parada");
		alert.setContentText(message.toString());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}

	private boolean showConfirmAlert(User user, Stop stop) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Registrar parada");
		alert.setHeaderText("¿Confirma los datos de la parada?");
		alert.setContentText("Parada: " + stop.getName() + " " + stop.getRegion() + "\nResponsable: "
				+ stop.getManager() + "\nEmail: " + user.getEmail());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/confirm.png")));
		alert.showAndWait();
		return alert.getResult().getButtonData().isDefaultButton();
	}

	

	private boolean validateData() {
		boolean ret = false;
		StringBuilder message = new StringBuilder();
		String name = stopName.getText();
		String manager = managerName.getText();
		String email = managerEmail.getText();
		String password = managerPassword.getText();
		String confirmPassword = confirmManagerPassword.getText();
		User emailExists = userService.findByEmail(email);

		// Validar nombre de la parada
		if (name.isEmpty()) {
			message.append("El nombre de la parada no puede estar vacío.\n");
		} else if (name.length() > 20) {
			message.append("El nombre de la parada no puede tener más de 20 caracteres.\n");
		} else if (name.chars().anyMatch(Character::isDigit)) {
			message.append("El nombre de la parada no puede contener números.\n");
		}

		// Validar nombre del responsable
		if (manager.isEmpty()) {
			message.append("El nombre del responsable no puede estar vacío.\n");
		} else if (manager.length() > 20) {
			message.append("El nombre del responsable no puede tener más de 20 caracteres.\n");
		} else if (manager.chars().anyMatch(Character::isDigit)) {
			message.append("El nombre del responsable no puede contener números.\n");
		}

		// Validar región
		if (cbregion.getValue() == null) {
			message.append("Debes seleccionar una región.\n");
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

		if (message.length() > 0) {
			showErrorAlert(message);
		} else {
			ret = true;
		}
		return ret;
	}

	public String getManagerName() {
		return managerName.getText();
	}

	public String getStopName() {
		return stopName.getText();
	}

	public String getRegion() {
		return cbregion.getSelectionModel().getSelectedItem();
	}

	public String getManagerEmail() {
		return managerEmail.getText();
	}

	public String getManagerPassword() {
		return managerPassword.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		User user = session.getLoggedInUser();

		if (user != null) {
			loggedInUser.setText("Usuario: " + user.getUsername());
		}

		loadNationalities();

		cbregion.setItems(regions);

		stopTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		setColumnProperties();

		// Add all stops into table
		loadStopDetails();
	}

	/*
	 * Set All userTable column properties
	 */
	private void setColumnProperties() {

		colStopId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colStopName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colStopRegion.setCellValueFactory(new PropertyValueFactory<>("region"));
		colManagerEmail.setCellValueFactory(new PropertyValueFactory<>("manager"));
		colManagerId.setCellValueFactory(new PropertyValueFactory<>("userId"));
	}

	private void loadStopDetails() {
		stopList.clear();
		stopList.addAll(stopService.findAll());

		stopTable.setItems(stopList);
	}

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
				regions.add(countryName);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
