package com.davidmb.tarea3ADbase.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * 
 */

@Controller
public class StopController implements Initializable {

	@FXML
	private Button btnLogout;

	@FXML
	private Label stopId;

	@FXML
	private ComboBox<String> cbPilgrims;

	@FXML
	private Button reset;

	@FXML
	private Button stampCard;

	@FXML
	private TableView<Pilgrim> pilgrimsTable;

	@FXML
	private TableColumn<Stop, Long> colStopId;

	@FXML
	private TableColumn<Stop, String> colPilgrimName;

	@FXML
	private TableColumn<Stop, String> colPilgrimNationality;

	@FXML
	private TableColumn<Stop, LocalDate> colDoS;
	
	@FXML
	private TableColumn<Stop, ImageView> colStay;
	
	@FXML
	private TableColumn<Stop, ImageView> colVip;

	@FXML
	private TableColumn<Stop, Boolean> colEdit;

	@FXML
	private MenuItem deleteStops;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private PilgrimService pilgrimService;
	@Autowired
	private UserService userService;

	private ObservableList<Pilgrim> pilgrimsList = FXCollections.observableArrayList();
	private ObservableList<String> pilgrims = FXCollections.observableArrayList();

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
	private void stampCard(ActionEvent event) {
		
		if (validateData()) {
				Pilgrim stop = pilgrimService.find(null);
			

				Pilgrim newPilgrimStop = pilgrimService.save(stop);

				saveAlert(newPilgrimStop);
			}

			clearFields();
			loadPilgrims();
		}
	

		

	

	@FXML
	private void deleteStops(ActionEvent event) {
		List<Pilgrim> pilgrims = pilgrimsTable.getSelectionModel().getSelectedItems();

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK)
			pilgrimService.deleteInBatch(pilgrims);

		loadPilgrims();
	}

	private void clearFields() {
		stopId.setText(null);
		cbPilgrims.getSelectionModel().clearSelection();
	}
	
	

	private void saveAlert(Pilgrim pilgrim) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Parada del peregrino registrada con éxito.");
		alert.setHeaderText("Parada del peregrino registrada con éxito.");
		alert.setContentText("La parada del peregrino " + pilgrim.getName() + " con ID: " + pilgrim.getId() + " ha sido registrada  \n");
		alert.showAndWait();
	}
	
	private void showErrorAlert(StringBuilder message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al sellar el carnet del peregrino");
		alert.setHeaderText("Error al sellar el carnet del peregrino");
		alert.setContentText(message.toString());
		alert.showAndWait();
	}
	
	private boolean validateData() {
		boolean ret = false;
		StringBuilder message = new StringBuilder();
        
        // Validar región
		if (cbPilgrims.getValue() == null) {
			message.append("Debes seleccionar un peregrino.\n");
		}

		if (message.length() > 0) {
			showErrorAlert(message);
		} else {
			ret = true;
		}
		return ret;
	}
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		cbPilgrims.setItems(pilgrims);

		pilgrimsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		setColumnProperties();

		loadPilgrims();
	}

	/*
	 * Set All userTable column properties
	 */
	private void setColumnProperties() {
		
		colStopId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colPilgrimName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colPilgrimNationality.setCellValueFactory(new PropertyValueFactory<>("nationality"));
		colManagerEmail.setCellValueFactory(new PropertyValueFactory<>("manager"));
		colManagerId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colEdit.setCellFactory(cellFactory);
	}

	Callback<TableColumn<Pilgrim, Boolean>, TableCell<Pilgrim, Boolean>> cellFactory = new Callback<TableColumn<Pilgrim, Boolean>, TableCell<Pilgrim, Boolean>>() {
		@Override
		public TableCell<Pilgrim, Boolean> call(final TableColumn<Pilgrim, Boolean> param) {
			final TableCell<Pilgrim, Boolean> cell = new TableCell<Pilgrim, Boolean>() {
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
	private void loadPilgrims() {
		pilgrimsList.clear();
		pilgrimsList.addAll(pilgrimService.findAll());

		pilgrimsTable.setItems(pilgrimsList);
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
