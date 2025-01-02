package com.davidmb.tarea3ADbase.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.User;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * @author Ram Alapure
 * @since 05-04-2017
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
	private TextField managerEmail;

	@FXML
	private PasswordField managerPassword;

	@FXML
	private Button reset;

	@FXML
	private Button saveStop;

	@FXML
	private TableView<User> stopTable;

	@FXML
	private TableColumn<User, Long> colStopId;

	@FXML
	private TableColumn<User, String> colStopName;

	@FXML
	private TableColumn<User, String> colStopRegion;

	@FXML
	private TableColumn<User, String> colManagerEmail;
	
	@FXML
	private TableColumn<User, String> colManagerId;

	@FXML
	private TableColumn<User, Boolean> colEdit;

	@FXML
	private MenuItem deleteStops;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private UserService userService;

	private ObservableList<User> userList = FXCollections.observableArrayList();
	private ObservableList<String> roles = FXCollections.observableArrayList("Admin", "User");

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}

	/**
	 * Logout and go to the login page
	 */
	@FXML
	private void logout(ActionEvent event) throws IOException {
		stageManager.switchScene(FxmlView.LOGIN);
	}

	@FXML
	void reset(ActionEvent event) {
		clearFields();
	}

	@FXML
	private void saveStop(ActionEvent event) {

//		if (validate("First Name", getFirstName(), "[a-zA-Z]+") && validate("Last Name", getLastName(), "[a-zA-Z]+")) {
//
//			if (userId.getText() == null || userId.getText() == "") {
//				if (validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")
//						&& emptyValidation("Password", getPassword().isEmpty())) {
//
//					User user = new User();
//					user.setFirstName(getFirstName());
//					user.setLastName(getLastName());
//					user.setDob(getDob());
//					user.setGender(getGender());
//					user.setRole(getRole());
//					user.setEmail(getEmail());
//					user.setPassword(getPassword());
//
//					User newUser = userService.save(user);
//
//					saveAlert(newUser);
//				}
//
//			} else {
//				User user = userService.find(Long.parseLong(userId.getText()));
//				user.setFirstName(getStopName());
//				//user.setLastName(getStopRegion());
//				user.setDob(getDob());
//				user.setGender(getGender());
//				user.setRole(getRole());
//				User updatedUser = userService.update(user);
//				updateAlert(updatedUser);
//			}

//			clearFields();
//			loadUserDetails();
//		}

	}

	@FXML
	private void deleteStops(ActionEvent event) {
		List<User> users = stopTable.getSelectionModel().getSelectedItems();

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK)
			userService.deleteInBatch(users);

		loadUserDetails();
	}

	private void clearFields() {
		stopId.setText(null);
		stopName.clear();
		cbregion.getSelectionModel().clearSelection();
		managerEmail.clear();
		managerPassword.clear();
	}

	private void saveAlert(User user) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User saved successfully.");
		alert.setHeaderText(null);
//		alert.setContentText("The user " + user.getFirstName() + " " + user.getLastName() + " has been created and \n"
//				+ getManagerEmail(user.getGender()) + " id is " + user.getId() + ".");
		alert.showAndWait();
	}

	private void updateAlert(User user) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User updated successfully.");
		alert.setHeaderText(null);
	//	alert.setContentText("The user " + user.getFirstName() + " " + user.getLastName() + " has been updated.");
		alert.showAndWait();
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

		cbregion.setItems(roles);

		stopTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		setColumnProperties();

		// Add all users into table
		loadUserDetails();
	}

	/*
	 * Set All userTable column properties
	 */
	private void setColumnProperties() {
		/*
		 * Override date format in table
		 * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new
		 * StringConverter<LocalDate>() { String pattern = "dd/MM/yyyy";
		 * DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		 * 
		 * @Override public String toString(LocalDate date) { if (date != null) { return
		 * dateFormatter.format(date); } else { return ""; } }
		 * 
		 * @Override public LocalDate fromString(String string) { if (string != null &&
		 * !string.isEmpty()) { return LocalDate.parse(string, dateFormatter); } else {
		 * return null; } } }));
		 */

		colStopId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colStopName.setCellValueFactory(new PropertyValueFactory<>("Nombre Parada"));
		colStopRegion.setCellValueFactory(new PropertyValueFactory<>("Región Parada"));
		colManagerEmail.setCellValueFactory(new PropertyValueFactory<>("Manager Email"));
		colManagerId.setCellValueFactory(new PropertyValueFactory<>("Manager Id"));
		colEdit.setCellFactory(cellFactory);
	}

	Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>> cellFactory = new Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>>() {
		@Override
		public TableCell<User, Boolean> call(final TableColumn<User, Boolean> param) {
			final TableCell<User, Boolean> cell = new TableCell<User, Boolean>() {
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
							User user = getTableView().getItems().get(getIndex());
							updateUser(user);
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

				private void updateUser(User user) {
					stopId.setText(Long.toString(user.getId()));
					//stopName.setText(user.getFirstName());		
					cbregion.getSelectionModel().select(user.getRole());
					managerEmail.setText(user.getEmail());
					managerPassword.setText(user.getPassword());
				}
			};
			return cell;
		}
	};

	/*
	 * Add All users to observable list and update table
	 */
	private void loadUserDetails() {
		userList.clear();
		userList.addAll(userService.findAll());

		stopTable.setItems(userList);
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
