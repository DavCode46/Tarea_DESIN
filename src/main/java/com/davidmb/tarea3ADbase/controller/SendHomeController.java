package com.davidmb.tarea3ADbase.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.models.Address;
import com.davidmb.tarea3ADbase.models.SendHome;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.SendHomeService;
import com.davidmb.tarea3ADbase.services.StopService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

@Controller
public class SendHomeController implements Initializable {

	@FXML
	private TextField txtPilgrimName;

	@FXML
	private TextField txtServices;

	@FXML
	private TextField txtWeight;

	@FXML
	private TextField txtHeight;

	@FXML
	private TextField txtWidth;

	@FXML
	private TextField txtDepth;

	@FXML
	private TextField txtAddress;

	@FXML
	private TextField txtLocation;

	@FXML
	private CheckBox chkUrgent;

	@FXML
	private Button btnSend;

	@FXML
	private Button btnReset;

	@FXML
	private TableView<SendHome> sendsHomeTable;

	@FXML
	private TableColumn<SendHome, String> colAddress;

	@FXML
	private TableColumn<SendHome, String> colLocation;

	@FXML
	private TableColumn<SendHome, Double> colWeight;

	@FXML
	private TableColumn<SendHome, String> colVolum;

	@FXML
	private TableColumn<SendHome, Boolean> colUrgent;

	private ObservableList<SendHome> sendHomesData = FXCollections.observableArrayList();

	@Autowired
	private SendHomeService sendHomeService;

	@Autowired
	private StopService stopService;

	@Autowired
	private Session session;

	private User user;

	private Stop currentStop;

	public void setPilgrimName(String pilgrimName) {
		txtPilgrimName.setText(pilgrimName);
	}

	public void setServices(String services) {
		txtServices.setText(services);
	}

	@FXML
	public void sendHome() {
		if (validateData()) {

			double weight = Double.parseDouble(txtWeight.getText());
			String height = txtHeight.getText();
			String width = txtWidth.getText();
			String depth = txtDepth.getText();
			String address = txtAddress.getText();
			String location = txtLocation.getText();
			boolean urgent = chkUrgent.isSelected();
			int[] volume = new int[3];
			volume[0] = Integer.parseInt(width);
			volume[1] = Integer.parseInt(height);
			volume[2] = Integer.parseInt(depth);
			Address addressObject = new Address(address, location);

			SendHome sendHome = new SendHome(weight, volume, urgent, addressObject, currentStop.getId());
			if(confirmAlert(sendHome)) {
				showSaveAlert(sendHome);
				sendHomeService.save(sendHome);
				reset();
			} else {
				showInfoAlert();
			}
			
			loadSendHomeData();
		}
	}

	private void setColumnProperties() {
		colAddress.setCellValueFactory(new PropertyValueFactory<>("addressStreet"));
		colLocation.setCellValueFactory(new PropertyValueFactory<>("addressLocality"));
		colWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
		colVolum.setCellValueFactory(new PropertyValueFactory<>("volumeFormatted"));
		colUrgent.setCellValueFactory(new PropertyValueFactory<>("urgent"));

		colUrgent.setCellFactory(column -> new TableCell<SendHome, Boolean>() {
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
	}

	@FXML
	public void reset() {
		txtWeight.setText("");
		txtHeight.setText("");
		txtWidth.setText("");
		txtDepth.setText("");
		txtAddress.setText("");
		txtLocation.setText("");
		chkUrgent.setSelected(false);
	}

	private boolean validateData() {
	    StringBuilder message = new StringBuilder();
	    String weight = txtWeight.getText().trim();
	    String height = txtHeight.getText().trim();
	    String width = txtWidth.getText().trim();
	    String depth = txtDepth.getText().trim();
	    String address = txtAddress.getText().trim();
	    String location = txtLocation.getText().trim();

	   
	    if (weight.isEmpty()) message.append("El peso es obligatorio\n");
	    if (height.isEmpty()) message.append("La altura es obligatoria\n");
	    if (width.isEmpty()) message.append("El ancho es obligatorio\n");
	    if (depth.isEmpty()) message.append("El largo es obligatorio\n");
	    if (address.isEmpty()) message.append("La dirección es obligatoria\n");
	    if (location.isEmpty()) message.append("La ubicación es obligatoria\n");

	   
	    Double peso = parseDouble(weight, "El peso debe ser un número válido", message);
	    Integer altura = parseInteger(height, "La altura debe ser un número entero válido", message);
	    Integer ancho = parseInteger(width, "El ancho debe ser un número entero válido", message);
	    Integer largo = parseInteger(depth, "El largo debe ser un número entero válido", message);

	  
	    if (peso != null && peso <= 0) message.append("El peso tiene que ser superior a 0\n");
	    if (altura != null && altura <= 0) message.append("La altura tiene que ser superior a 0\n");
	    if (ancho != null && ancho <= 0) message.append("El ancho tiene que ser superior a 0\n");
	    if (largo != null && largo <= 0) message.append("El largo tiene que ser superior a 0\n");

	   
	    if (message.length() > 0) {
	        showErrorAlert(message.toString());
	        return false;
	    }

	    return true;
	}

	
	private Double parseDouble(String value, String errorMessage, StringBuilder message) {
	    try {
	        return value.isEmpty() ? null : Double.parseDouble(value);
	    } catch (NumberFormatException e) {
	        message.append(errorMessage).append("\n");
	        return null;
	    }
	}

	
	private Integer parseInteger(String value, String errorMessage, StringBuilder message) {
	    try {
	        return value.isEmpty() ? null : Integer.parseInt(value);
	    } catch (NumberFormatException e) {
	        message.append(errorMessage).append("\n");
	        return null;
	    }
	}


	private void showSaveAlert(SendHome sendHome) {
		Alert alert = new Alert(AlertType.INFORMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
		alert.setTitle("Envío registrado");
		alert.setHeaderText("Envío registrado con éxito");
		alert.setContentText("Su envío está en camino, recibirá un mensaje con el tracking");
		alert.showAndWait();
	}
	
	private void showInfoAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/info.png")));
		alert.setTitle("Información");
		alert.setHeaderText("Información");
		alert.setContentText("El envío ha sido cancelado");
		alert.showAndWait();
	}

	private boolean confirmAlert(SendHome sendHome) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirma tus datos");
		alert.setHeaderText("Confirma tus datos");
		alert.setContentText("¿Estás seguro de que quieres enviar el paquete con los siguientes datos?\n\n" + "Peso: "
				+ sendHome.getWeight() + " kg\n" + "Dimensiones: " + sendHome.getVolume()[1] + "x"
				+ sendHome.getVolume()[0] + "x" + sendHome.getVolume()[2] + " cm\n" + "Dirección: "
				+ sendHome.getAddress().getStreet() + ", " + sendHome.getAddress().getLocality() + "\n" + "Urgente: "
				+ (sendHome.isUrgent() ? "Sí" : "No") + "\n\n" + "Una vez enviado, no podrás modificar los datos.");

		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/confirm.png")));
		alert.showAndWait();
		return alert.getResult().getButtonData().isDefaultButton();
	}

	private void showErrorAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error al enviar el paquete");
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		user = session.getLoggedInUser();
		currentStop = stopService.findByUserId(user.getId());
		setColumnProperties();
		loadSendHomeData();
	}

	private void loadSendHomeData() {
		sendsHomeTable.getItems().clear();
		sendHomesData.addAll(sendHomeService.getAllByStopId(currentStop.getId()));
		sendsHomeTable.setItems(sendHomesData);
	}
}
