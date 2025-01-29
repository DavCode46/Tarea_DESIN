package com.davidmb.tarea3ADbase.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.dtos.ServiceResponse;
import com.davidmb.tarea3ADbase.dtos.StayView;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.PilgrimStops;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.PilgrimStopsService;
import com.davidmb.tarea3ADbase.services.StopService;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
	private CheckBox cbStay;

	@FXML
	private RadioButton rbYes;

	@FXML
	private RadioButton rbNo;

	@FXML
	private DatePicker dpStart;

	@FXML
	private DatePicker dpEnd;

	@FXML
	private ComboBox<String> cbPilgrims;

	@FXML
	private Button reset;

	@FXML
	private Button stampCard;

	@FXML
	private TableView<StayView> pilgrimsTable;

	@FXML
	private TableColumn<StayView, String> colPilgrimName;

	@FXML
	private TableColumn<StayView, String> colPilgrimNationality;
	
	@FXML
	private TableColumn<StayView, String> colStopDate;

	@FXML
	private TableColumn<StayView, LocalDate> colDoS;

	@FXML
	private TableColumn<StayView, Boolean> colStay;

	@FXML
	private TableColumn<StayView, Boolean> colVip;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private PilgrimService pilgrimService;

	@Autowired
	private StopService stopService;

	@Autowired
	private PilgrimStopsService pilgrimStopsService;

	@Autowired
	private Session session;

	private User user;

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
	private void filterStays() {
		if (dpStart.getValue() != null && dpEnd.getValue() != null) {
			if (dpStart.getValue().isBefore(dpEnd.getValue())) {
				ObservableList<StayView> pilgrimStaysByDate = FXCollections.observableArrayList();
				List<StayView> stayViewList = pilgrimService.findStayViewsByStopBetweenDates(
						stopService.findByUserId(user.getId()).getId(), dpStart.getValue(), dpEnd.getValue());
				pilgrimStaysByDate.addAll(stayViewList);
				pilgrimsTable.setItems(pilgrimStaysByDate);
				dpStart.setValue(null);
				dpEnd.setValue(null);
			} else {
				showErrorAlert(new StringBuilder("La fecha de inicio debe ser anterior a la fecha de fin"),
						new String("Error al filtrar paradas"));
			}
		} else {
			showErrorAlert(new StringBuilder("Debes seleccionar una fecha de inicio y una fecha de fin para filtrar"),
					new String("Error al filtrar paradas"));
		}
	}

	@FXML
	private void clearFilters() {
		dpStart.setValue(null);
		dpEnd.setValue(null);
		loadStayViews();
	}

	@FXML
	void reset(ActionEvent event) {
		clearFields();
	}

	@FXML
	private void stampCard(ActionEvent event) {
		if (validateData()) {
			if (!cbStay.isSelected()) {
				rbNo.setSelected(true);
			}

			Pilgrim pilgrim = pilgrimService.find(Long.valueOf(cbPilgrims.getValue().split(" ")[1]));
			Stop stop = stopService.findByUserId(user.getId());

			boolean existsPilgrimStop = pilgrimStopsService.existsByPilgrimAndStopAndStopDate(pilgrim, stop,
					LocalDate.now());
			if (!existsPilgrimStop) {

				ServiceResponse<Pilgrim> serviceResponse = pilgrimService.stampCard(pilgrim, stop, rbYes.isSelected(),
						cbStay.isSelected());
				if ((serviceResponse != null)) {
					if (serviceResponse.isSuccess()) {
						saveAlert(serviceResponse, pilgrim);
					} else {
						showServiceResponseError(serviceResponse, pilgrim);
					}
					clearFields();
				} else {
					showErrorAlert(new StringBuilder("Error al sellar el carnet del peregrino"),
							new String("Error al sellar el Carnet"));
				}
				loadStayViews();
			} else {
				showErrorAlert(new StringBuilder("El peregrino ya ha sellado su carnet en esta parada"),
						new String("Error al sellar el Carnet"));
			}
		}
	}

	private void clearFields() {
		stopId.setText(null);
		cbPilgrims.getSelectionModel().clearSelection();
	}

	private void saveAlert(ServiceResponse<Pilgrim> serviceResponse, Pilgrim pilgrim) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(serviceResponse.getMessage());
		alert.setHeaderText(serviceResponse.getMessage());
		alert.setContentText("La parada del peregrino " + pilgrim.getName() + " con ID: " + pilgrim.getId()
				+ " ha sido registrada  \n");

		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));

		alert.showAndWait();
	}

	private void showServiceResponseError(ServiceResponse<Pilgrim> serviceResponse, Pilgrim pilgrim) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al sellar el carnet del peregrino");
		alert.setHeaderText("Ha ocurrido un error al sellar el carnet del peregrino");
		alert.setContentText("Ha ocurrido un error al sellar el carnet del peregrino " + pilgrim.getName() + " con ID: "
				+ pilgrim.getId() + "\n" + serviceResponse.getMessage());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}

	private void showErrorAlert(StringBuilder message, String header) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(message.toString());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}

	private boolean validateData() {
		boolean ret = false;
		StringBuilder message = new StringBuilder();

		// Validar peregrino
		if (cbPilgrims.getValue() == null) {
			message.append("Debes seleccionar un peregrino.\n");
		}

		if (message.length() > 0) {
			showErrorAlert(message, "Error al sellar el carnet del peregrino");
		} else {
			ret = true;
		}
		return ret;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		user = session.getLoggedInUser();

		stopId.setText("Parada: " + user.getUsername());

		loadPilgrims();

		pilgrimsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		setColumnProperties();

		loadStayViews();
	}

	/*
	 * Set All userTable column properties
	 */
	private void setColumnProperties() {

		colPilgrimName.setCellValueFactory(new PropertyValueFactory<>("pilgrimName"));
		colPilgrimNationality.setCellValueFactory(new PropertyValueFactory<>("pilgrimNationality"));
		colStopDate.setCellValueFactory(new PropertyValueFactory<>("stopDate"));

		colStay.setCellValueFactory(new PropertyValueFactory<>("stay"));

		colStay.setCellFactory(column -> new TableCell<StayView, Boolean>() {
			private final ImageView imageView = new ImageView();

			@Override
			protected void updateItem(Boolean stay, boolean empty) {
				super.updateItem(stay, empty);
				if (empty || stay == null) {
					setGraphic(null);
				} else {
					Image image = new Image(stay ? "/icons/check.png" : "/icons/cross.png");
					imageView.setImage(image);
					imageView.setFitHeight(16);
					imageView.setFitWidth(16);
					setGraphic(imageView);
				}
			}
		});

		colDoS.setCellValueFactory(new PropertyValueFactory<>("stayDate"));

		colVip.setCellValueFactory(new PropertyValueFactory<>("isVip"));
		colVip.setCellFactory(column -> new TableCell<StayView, Boolean>() {
			private final ImageView imageView = new ImageView();

			@Override
			protected void updateItem(Boolean isVip, boolean empty) {
				super.updateItem(isVip, empty);
				if (empty || isVip == null) {
					setGraphic(null);
				} else {
					Image image = new Image(isVip ? "/icons/check.png" : "/icons/cross.png");
					imageView.setImage(image);
					imageView.setFitHeight(16);
					imageView.setFitWidth(16);
					setGraphic(imageView);
				}
			}
		});

	}

	/*
	 * Add All users to observable list and update table
	 */
	private void loadStayViews() {
		ObservableList<StayView> stayViews = FXCollections.observableArrayList();

		List<StayView> stayViewList = pilgrimService
				.findAllStayViewsByStop(stopService.findByUserId(user.getId()).getId());
		stayViews.addAll(stayViewList);	
		pilgrimsTable.setItems(stayViews);
	}

	private void loadPilgrims() {
		cbPilgrims.getItems().clear();
		pilgrimService.findAll()
				.forEach(pilgrim -> cbPilgrims.getItems().addAll("ID: " + pilgrim.getId() + " - " + pilgrim.getName()));
	}

	/*
	 * Validations
	 */

}
