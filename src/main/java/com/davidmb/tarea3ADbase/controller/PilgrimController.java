package com.davidmb.tarea3ADbase.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.StayService;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.utils.ExportarCarnetXML;
import com.davidmb.tarea3ADbase.utils.HelpUtil;
import com.davidmb.tarea3ADbase.utils.ManagePassword;
import com.davidmb.tarea3ADbase.utils.ShowPDFInModal;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
public class PilgrimController implements Initializable {
	
	@FXML
	public TextField userName; 
	@FXML
	public TextField userEmail;
	
	@FXML
	public PasswordField userPassword;
	
	@FXML
	public TextField userPasswordVisibleField;
	
	@FXML
	public PasswordField confirmUserPassword;
	
	@FXML
	public TextField confirmUserPasswordVisibleField;
	
	@FXML
	public CheckBox showPasswordCheckBox;

	@FXML
	public Label lblName;

	@FXML
	public Label lblNationality;

	@FXML
	public Label lblDoExp;

	@FXML
	public Label lblDistance;

	@FXML
	public Label lnlnVips;

	@FXML
	public Label lblInitialStop;
	
	@FXML
	public Button logoutBtn;
	
	@FXML
	public Button exportCarnetBtn;
	
	@FXML
	public Button exportReportBtn;
	
	@FXML
	public Button resetBtn;
	
	@FXML
	public Button updateUserBtn;
	
	@FXML
	public Button btnHelp;

	@FXML
	public ImageView avatarImageView;

	@Autowired
	private PilgrimService pilgrimService;
	
	@Autowired
	private ExportarCarnetXML exportarCarnet;
	
	@Autowired 
	StayService stayService;
	
	@Autowired 
	StopService stopService;

	@Autowired
	private Session session;
	
	@Autowired
	private UserService userService;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnHelp.setTooltip(new Tooltip("Pulsa F1 para ver la ayuda"));
		exportCarnetBtn.setTooltip(new Tooltip("Exportar carnet en XML"));
		exportReportBtn.setTooltip(new Tooltip("Exportar Informe del carnet"));
		logoutBtn.setTooltip(new Tooltip("Cerrar sesión"));
		User user = session.getLoggedInUser();
		System.out.println(user);
		loadUserProfile(user);
	}
	
	@FXML
	private void showHelp() {
		HelpUtil.showHelp();
	}
	
	@FXML
	private void reset() {
		clearFields();
	}
	

	private void clearFields() {
		userEmail.clear();
		userPassword.clear();
		confirmUserPassword.clear();
		userPasswordVisibleField.clear();
		confirmUserPasswordVisibleField.clear();
	}
	
	@FXML
	public void updateUser() {
		if(validateData()) {
			User user = session.getLoggedInUser();
			Pilgrim pilgrim = pilgrimService.findByUserId(user.getId());
			user.setEmail(userEmail.getText());
			if (userPassword.isVisible()) {
				user.setPassword(userPassword.getText());
			} else {
				user.setPassword(userPasswordVisibleField.getText());
			}
			if (showConfirmAlert(user, pilgrim)) {
				userService.save(user);
				saveAlert(pilgrim, user);
				clearFields();
				loadUserProfile(user);
			} else {
				showInfoAlert("Operación cancelada", "Operación cancelada", "No se han guardado los cambios.");
			}
		} else {
			showErrorAlert("Error", "Error al modificar los datos.");
		}
	}
	
	
	public boolean validateData() {
		boolean ret = false;
		StringBuilder message = new StringBuilder();
		String email = userEmail.getText();
		String password = userPassword.getText();
		String visiblePassword = userPasswordVisibleField.getText();
		String confirmPassword = confirmUserPassword.getText();
		String confirmPasswordVisible = confirmUserPasswordVisibleField.getText();
		User emailExists = userService.findByEmail(email);

	

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
		if (userPassword.isVisible()) {
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
		} else {
			if (visiblePassword.isEmpty()) {
				message.append("La contraseña no puede estar vacía.\n");
			} else if (visiblePassword.length() < 8) {
				message.append("La contraseña debe tener al menos 8 caracteres.\n");
			} else if (!visiblePassword.matches(".*\\d.*")) {
				message.append("La contraseña debe contener al menos un número.\n");
			} else if (!visiblePassword.matches(".*[a-z].*")) {
				message.append("La contraseña debe contener al menos una letra minúscula.\n");
			} else if (!visiblePassword.matches(".*[A-Z].*")) {
				message.append("La contraseña debe contener al menos una letra mayúscula.\n");
			} else if (!visiblePassword.matches(".*[!@#$%^&*].*")) {
				message.append("La contraseña debe contener al menos un carácter especial.\n");
			} else if (!visiblePassword.equals(confirmPasswordVisible)) {
				message.append("Las contraseñas no coinciden.\n");
			}
		}

		if (message.length() > 0) {
			showErrorAlert(message);
		} else {
			ret = true;
		}
		return ret;
	}
	
	public void showInfoAlert(String title, String header, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);
	
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
		alert.showAndWait();
	}
	
	public void saveAlert(Pilgrim pilgrim, User user) {

		Alert alert = new Alert(AlertType.INFORMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
		alert.setTitle("Datos modificados con éxito.");
		alert.setHeaderText("Datos modificados con éxito.");
		alert.setContentText("Nuevos datos de usuario:\nEmail: " + user.getEmail());

		alert.showAndWait();
	}

	private void showErrorAlert(StringBuilder message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al modificar los datos");
		alert.setHeaderText("Error al modificar los datos");
		alert.setContentText(message.toString());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}

	public boolean showConfirmAlert(User user, Pilgrim pilgrim) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Modificar datos");
		alert.setHeaderText("¿Confirma los nuevos datos?");
		alert.setContentText("Nombre: " + pilgrim.getName() + "\nEmail: " + user.getEmail() + "\nContraseña: "
                + user.getPassword());
		
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/confirm.png")));
		alert.showAndWait();
		return alert.getResult().getButtonData().isDefaultButton();
	}


	@FXML
	private void togglePasswordVisibility() {
		ManagePassword.showPassword(userPasswordVisibleField, userPassword, showPasswordCheckBox,
				confirmUserPasswordVisibleField, confirmUserPassword);
	}

	/**
	 * Cargar los datos del usuario y actualizarlos en la vista
	 */
	public void loadUserProfile(User user) {
		Pilgrim pilgrim = pilgrimService.findByUserId(user.getId());
		

		if (pilgrim != null) {
			Image avatar = new Image(getClass().getResourceAsStream("/images/pilgrim.png"));
			avatarImageView.setImage(avatar);
			lblName.setText(pilgrim.getName());
			lblNationality.setText("Nacionalidad: " + pilgrim.getNationality());
			lblDoExp.setText("Fecha Expedición: " + pilgrim.getCarnet().getDoExp().toString());
			lblDistance.setText("Distancia total: " + Double.valueOf(pilgrim.getCarnet().getDistance()).toString());
			lnlnVips.setText("Nº VIPS: " + Integer.valueOf(pilgrim.getCarnet().getnVips()).toString());
			lblInitialStop.setText("Parada Inicial: " + pilgrim.getCarnet().getInitialStop().getName());
			userName.setText(pilgrim.getName());
			userEmail.setText(user.getEmail());
		} else {
			showErrorAlert("Error", "El usuario no existe.");
		}
	}

	/**
	 * Exportar los datos del carnet
	 */
	@FXML
	private void exportCarnet() {
		exportarCarnet = new ExportarCarnetXML(stayService, stopService);
		try {
			exportarCarnet.exportarCarnet(pilgrimService.findByUserId(session.getLoggedInUser().getId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void exportCarnetReport() {
	    try {
	        Pilgrim pilgrim = pilgrimService.findByUserId(session.getLoggedInUser().getId());
	        String outputPath = "src/main/resources/reports/paradas/" + pilgrim.getName() + "-carnet.pdf";

	        InputStream reportStream = getClass().getResourceAsStream("/templates/report/ReportCarnet.jasper");
	        if (reportStream == null) {
	            throw new JRException("No se pudo encontrar el archivo Report.jasper en resources/templates/report.");
	        }

	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

	        Map<String, Object> params = new HashMap<>();
	        params.put("titulo", "Carnet" + pilgrim.getName());

	        URL imageUrl = getClass().getResource("/images/carnet.png");
	        if (imageUrl == null) {
	            throw new JRException("No se pudo encontrar la imagen carnet.png en resources/images.");
	        }

	        params.put("imagen", imageUrl.toExternalForm());

	        params.put("nombre", pilgrim.getName());
	        params.put("nacionalidad", pilgrim.getNationality());
	        params.put("fecha_expedicion", pilgrim.getCarnet().getDoExp().toString());
	        params.put("distancia", String.valueOf(pilgrim.getCarnet().getDistance()));
	        params.put("n_vips", String.valueOf(pilgrim.getCarnet().getnVips()));
	        params.put("nombre_parada", pilgrim.getCarnet().getInitialStop().getName());
	        params.put("region", pilgrim.getCarnet().getInitialStop().getRegion());

	        // Log de los parámetros
	        System.out.println("Parámetros del reporte:");
	        params.forEach((key, value) -> System.out.println(key + ": " + value));

	        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
	        JasperExportManager.exportReportToPdfFile(print, outputPath);

	        // Mostrar el PDF en una ventana modal
	        ShowPDFInModal showPDFInModal = new ShowPDFInModal();
	       showPDFInModal.showPdfInModal(outputPath);
	    } catch (JRException e) {
	        e.printStackTrace();
	        // Manejar la excepción adecuadamente
	    }
	}

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



	/**
	 * Alerta de error
	 */
	public void showErrorAlert(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		 // Cambiar el ícono de la ventana
	    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
	    alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}
}
