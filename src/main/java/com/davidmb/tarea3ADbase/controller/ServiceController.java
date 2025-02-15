package com.davidmb.tarea3ADbase.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.Service;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.utils.HelpUtil;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@Controller
public class ServiceController implements Initializable{
	
	@FXML
	private Button btnHelp;
	
	
	@FXML
	private Button btnReturn;
	
	@FXML
	private ListView<CheckBox> stopsListView;
	
	@FXML
	private Label loggedInUser;

	@FXML
	private TextField serviceName;

	@FXML
	private ComboBox<String> cbregion;

	@FXML
	private TextField servicePrice;


	@FXML
	private Button reset;

	@FXML
	private Button saveService;
	
	@FXML
	private TableView<Stop> stopsTable;

	@FXML
	private TableColumn<Stop, Long> colStopId;

	@FXML
	private TableColumn<Stop, String> colStopName;

	@FXML
	private TableColumn<Stop, String> colStopRegion;

	@FXML
	private TableView<Service> servicesTable;

	@FXML
	private TableColumn<Service, Long> colServiceId;

	@FXML
	private TableColumn<Service, String> colServiceName;

	@FXML
	private TableColumn<Service, String> colServicePrice;

	@FXML
	private TableColumn<Service, String> colServiceStops;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private Session session;

	@Autowired
	private StopService stopService;


	private ObservableList<Service> servicesList = FXCollections.observableArrayList();
	private ObservableList<Stop> stopList = FXCollections.observableArrayList();

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}
	
	@FXML
	private void showHelp() {
		HelpUtil.showHelp();
	}

	@FXML
	public void onReturn() {
		stageManager.switchScene(FxmlView.ADMIN);
	}

	@FXML
	void reset(ActionEvent event) {
		clearFields();
	}


	@FXML
	private void onSaveService(ActionEvent event) {

		
	}
	






	private void clearFields() {
		
	}

	private void saveAlert(Service service) {

		Alert alert = new Alert(AlertType.INFORMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		if (service != null) {
			alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
			alert.setTitle("Servicio registrado con éxito.");
			alert.setHeaderText("Servicio registrado con éxito.");
			alert.setContentText(
					"El servicio " + service.getServiceName() + " ha sido registrado con éxito.");
		} else {
			alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/info.png")));
			alert.setTitle("Registro cancelado");
			alert.setHeaderText("Registro cancelado");
			alert.setContentText("Registro del servicio cancelado, vuelve a introducir los datos");
		}
		// Cambiar el ícono de la ventana

		alert.showAndWait();
	}

	private void showErrorAlert(StringBuilder message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al registrar el servicio");
		alert.setHeaderText("Error al registrar el servicio");
		alert.setContentText(message.toString());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}

	private boolean showConfirmAlert(Service service) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Registrar servicio");
		alert.setHeaderText("¿Confirma los datos del servicio?");
		alert.setContentText("Servicio: " + service.getServiceName() + " Precio: " + service.getPrice());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/confirm.png")));
		alert.showAndWait();
		return alert.getResult().getButtonData().isDefaultButton();
	}

	private boolean validateData() {
		boolean ret = false;
		StringBuilder message = new StringBuilder();
		

		
		return ret;
	}

	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		loadStopDetails();

		User user = session.getLoggedInUser();

		if (user != null) {
			loggedInUser.setText("Usuario: " + user.getUsername());
		}

		

		servicesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		stopsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		setColumnProperties();

		// Add all stops into table
		loadServicesDetail();
	}

	/*
	 * Set All userTable column properties
	 */
	private void setColumnProperties() {

		colServiceId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colServiceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
		colServicePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colServiceStops.setCellValueFactory(new PropertyValueFactory<>("stopsIds"));
		
		
		colStopId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colStopName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colStopRegion.setCellValueFactory(new PropertyValueFactory<>("region"));
	}

	private void loadServicesDetail() {
		servicesList.clear();
		// servicesList.addAll();

		servicesTable.setItems(servicesList);
	}
	
	private void loadStopDetails() {
		stopList.clear();
		stopList.addAll(stopService.findAll());

		stopsTable.setItems(stopList);
	}
	
	  @FXML
	    private void getSelectedStops() {
	        ObservableList<Stop> selectedStops = stopsTable.getSelectionModel().getSelectedItems();
	        for (Stop stop : selectedStops) {
	            System.out.println("Parada seleccionada: " + stop.getName());
	        }
	    }


}
