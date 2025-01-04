package com.davidmb.tarea3ADbase.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * 
 */

@Controller
public class AdminController implements Initializable {

	@FXML
	private Button btnLogout;

	@FXML
	private Label stopId;

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
	private PasswordField confirmManagerPassword;

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
	private TableColumn<Stop, Boolean> colEdit;

	@FXML
	private MenuItem deleteStops;

	@Lazy
	@Autowired
	private StageManager stageManager;

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

			if (stopId.getText() == null || stopId.getText() == "") {
				Stop stop = new Stop();
				User user = new User();
				user.setUsername(getManagerName());
				user.setEmail(getManagerEmail());
				user.setPassword(getManagerPassword());
				user.setRole("Parada");
				User newUser = userService.save(user);
				
				stop.setName(getStopName());
				stop.setRegion(getRegion().charAt(0));
				stop.setManager(newUser.getUsername());
				stop.setUserId(newUser.getId());

				Stop newStop = stopService.save(stop);

				saveAlert(newStop);
			} else {
				Stop stop = stopService.find(Long.parseLong(stopId.getText()));
				stop.setName(getStopName());
				stop.setRegion(getRegion().charAt(0));
				stop.setManager(userService.find(stop.getUserId()).getUsername());
				stop.setUserId(userService.find(stop.getUserId()).getId());

				Stop updatedStop = stopService.update(stop);
				updateAlert(updatedStop);
			}

			clearFields();
			loadStopDetails();
		}
	}
		

	

	@FXML
	private void deleteStops(ActionEvent event) {
		List<Stop> users = stopTable.getSelectionModel().getSelectedItems();

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK)
			stopService.deleteInBatch(users);

		loadStopDetails();
	}

	private void clearFields() {
		stopId.setText(null);
		stopName.clear();
		managerName.clear();
		cbregion.getSelectionModel().clearSelection();
		managerEmail.clear();
		managerPassword.clear();
		confirmManagerPassword.clear();
	}
	
	

	private void saveAlert(Stop stop) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Parada registrada con éxito.");
		alert.setHeaderText("Parada registrada con éxito.");
		alert.setContentText("La parada " + stop.getName() + " " + stop.getRegion() + " ha sido creada y el responsable es \n"
				+ getManagerEmail() + " con id " +  + userService.find(stop.getUserId()).getId() + ".");
		alert.showAndWait();
	}

	private void updateAlert(Stop stop) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Parada actualizada con éxito.");
		alert.setHeaderText("Parada actualizada con éxito.");
		alert.setContentText("Parada " + stop.getName() + " en región " + stop.getRegion() + " ha sido actualizada con éxito.");
		alert.showAndWait();
	}
	
	private void showErrorAlert(StringBuilder message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al registrar parada");
		alert.setHeaderText("Error al registrar parada");
		alert.setContentText(message.toString());
		alert.showAndWait();
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
        if(name.isEmpty()) {
        	message.append("El nombre de la parada no puede estar vacío.\n");
        } else if(name.length() > 20) {
        	message.append("El nombre de la parada no puede tener más de 20 caracteres.\n");
		} else if (name.chars().anyMatch(Character::isDigit)) {
			message.append("El nombre de la parada no puede contener números.\n");
		}
        
        // Validar nombre del responsable
        if(manager.isEmpty()) {
        	message.append("El nombre del responsable no puede estar vacío.\n");
        } else if(manager.length() > 20) {
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
        colEdit.setCellFactory(cellFactory);
	}

	Callback<TableColumn<Stop, Boolean>, TableCell<Stop, Boolean>> cellFactory = new Callback<TableColumn<Stop, Boolean>, TableCell<Stop, Boolean>>() {
		@Override
		public TableCell<Stop, Boolean> call(final TableColumn<Stop, Boolean> param) {
			final TableCell<Stop, Boolean> cell = new TableCell<Stop, Boolean>() {
				Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button();

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						btnEdit.setOnAction(e -> {
							Stop stop = getTableView().getItems().get(getIndex());
							updateStop(stop);
						});

						btnEdit.setStyle("-fx-background-color: transparent;");
						ImageView iv = new ImageView();
						iv.setImage(imgEdit);
						iv.setPreserveRatio(true);
						iv.setSmooth(true);
						iv.setCache(true);
						btnEdit.setGraphic(iv);

						setGraphic(btnEdit);
						setAlignment(Pos.CENTER);
						setText(null);
					}
				}

				private void updateStop(Stop stop) {
					stopId.setText(Long.toString(stop.getId()));
					stopName.setText(stop.getName());		
					cbregion.getSelectionModel().select(stop.getRegion());
					managerName.setText(userService.find(stop.getUserId()).getUsername());
					managerEmail.setText(userService.find(stop.getUserId()).getEmail());
					managerPassword.setText(userService.find(stop.getUserId()).getPassword());
				}
			};
			return cell;
		}
	};

	/*
	 * Add All users to observable list and update table
	 */
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

	/*
	 * Validations
	 */
	private boolean validate(String field, String value, String pattern) {
		if (!value.isEmpty()) {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(value);
			if (m.find() && m.group().equals(value)) {
				return true;
			} else {
				validationAlert(field, false);
				return false;
			}
		} else {
			validationAlert(field, true);
			return false;
		}
	}

	private boolean emptyValidation(String field, boolean empty) {
		if (!empty) {
			return true;
		} else {
			validationAlert(field, true);
			return false;
		}
	}

	private void validationAlert(String field, boolean empty) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Validation Error");
		alert.setHeaderText(null);
		if (field.equals("Role"))
			alert.setContentText("Please Select " + field);
		else {
			if (empty)
				alert.setContentText("Please Enter " + field);
			else
				alert.setContentText("Please Enter Valid " + field);
		}
		alert.showAndWait();
	}
}
