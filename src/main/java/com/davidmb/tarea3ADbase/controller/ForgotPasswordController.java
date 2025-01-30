package com.davidmb.tarea3ADbase.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@Controller
public class ForgotPasswordController implements Initializable{
	private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

	
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField passwordField;
    
    @FXML
    private TextField passwordField2;
    
    @FXML
    private Label lblPasswordError;
    
    @FXML
    private Label lblEmailError;
    
    @Lazy
    @Autowired
    private StageManager stageManager;
    
    @Autowired
    private UserService userService;
 
    @FXML
    private void resetPassword() {
        if (!validateData()) {
            return; 
        }

        String email = emailField.getText();
        String newPassword = passwordField.getText();
        User user = userService.findByEmail(email);

        try {
            
            userService.resetPassword(user, newPassword);
            showSuccessAlert(email);
            stageManager.switchScene(FxmlView.LOGIN);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }


    @FXML
    private void backToLogin() {
		// Cambiar a la vista de "Login"
		stageManager.switchScene(FxmlView.LOGIN);
    }
    
    private boolean validateData() {

        boolean ret = false;
		StringBuilder message = new StringBuilder();
		String email = emailField.getText();
		String password = passwordField.getText();
		String confirmPassword = passwordField2.getText();
		User emailExists = userService.findByEmail(email);
		
        // Validar Email
      		if (email.isEmpty()) {
      			message.append("El Email no puede estar vacío.\n");
      		} else if(emailExists == null) {
      			message.append("El Email no existe.\n");
      		}
      		else if (email.length() > 50) {
      			message.append("El Email no puede tener más de 50 caracteres.\n");
      		} else if (!email.matches(EMAIL_PATTERN)) {
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

    private void showSuccessAlert(String email) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Contraseña restablecida.");
		alert.setHeaderText("Contraseña restablecida.");
		alert.setContentText("La contraseña del usuario " + email + " se ha restablecido con éxito.");
		 // Cambiar el ícono de la ventana
	    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
	    alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
		alert.showAndWait();
	}
	
	private void showErrorAlert(StringBuilder message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error al restablecer la contraseña");
		alert.setContentText(message.toString());
		 // Cambiar el ícono de la ventana
	    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
	    alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}

    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
