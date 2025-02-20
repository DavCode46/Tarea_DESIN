package com.davidmb.tarea3ADbase.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.dtos.StayView;
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

//	@FXML
//	private TableColumn<SendHome, String> colPilgrimName;

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

			int weight = Integer.parseInt(txtWeight.getText());
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

			
			SendHome sendHome = new SendHome(10.0, weight, volume, urgent, addressObject, currentStop.getId());

			sendHomeService.save(sendHome);
			reset();
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
		String weight = txtWeight.getText();
		String height = txtHeight.getText();
		String width = txtWidth.getText();
		String depth = txtDepth.getText();
		String address = txtAddress.getText();
		String location = txtLocation.getText();


		if (weight.isEmpty()) {
			message.append("El peso es obligatorio\n");
		}

		if (height.isEmpty()) {
			message.append("La altura es obligatoria\n");
		}

		if (width.isEmpty()) {
			message.append("El ancho es obligatorio\n");
		}

		if (depth.isEmpty()) {
			message.append("La profundidad es obligatoria\n");
		}

		if (address.isEmpty()) {
			message.append("La dirección es obligatoria\n");
		}

		if (location.isEmpty()) {
			message.append("La ubicación es obligatoria\n");
		}

		if (message.length() > 0) {
			showErrorAlert(message.toString());
			return false;
		}

		return true;

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
