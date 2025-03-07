package com.davidmb.tarea3ADbase.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.utils.HelpUtil;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Controlador para la funcionalidad de restablecimiento de contraseña.
 * 
 * Permite a los usuarios restablecer su contraseña ingresando su correo electrónico 
 * y estableciendo una nueva contraseña. Incluye validaciones y alertas para notificar 
 * al usuario sobre el estado del proceso.
 * 
 * Implementa `Initializable` para la configuración inicial de la interfaz gráfica.
 * 
 * @author DavidMB
 */
@Controller
public class ForgotPasswordController implements Initializable{
	 /** Expresión regular para validar el formato del email. */
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
    private Button changePasswordBtn;
    
    @FXML
    private Button backToLoginBtn;
    
    @FXML
    private Button helpBtn;
    
    @FXML
    private Label lblEmailError;
    
    @Lazy
    @Autowired
    private StageManager stageManager;
    
    @Autowired
    private UserService userService;
 
    /**
     * Método que gestiona el proceso de restablecimiento de contraseña.
     * 
     * - Valida los datos ingresados.
     * - Verifica si el email existe en la base de datos.
     * - Restablece la contraseña del usuario si los datos son correctos.
     * - Muestra una alerta de confirmación o error según el resultado.
     */
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
    
    /**
     * Muestra la ventana de ayuda de la aplicación.
     */
    @FXML
	private void showHelp() {
		HelpUtil.showHelp();
	}


    /**
     * Vuelve a la pantalla de inicio de sesión.
     */
    @FXML
    private void backToLogin() {
		// Cambiar a la vista de "Login"
		stageManager.switchScene(FxmlView.LOGIN);
    }
    
    /**
     * Valida los datos ingresados en el formulario antes de proceder con el restablecimiento de contraseña.
     * 
     * - Verifica que el email no esté vacío y tenga el formato correcto.
     * - Comprueba si el email existe en la base de datos.
     * - Valida que la nueva contraseña cumpla con los requisitos de seguridad.
     * - Confirma que la contraseña y su confirmación coincidan.
     * 
     * @return `true` si los datos son válidos, `false` en caso contrario.
     */
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

    /**
     * Muestra una alerta informativa tras restablecer la contraseña correctamente.
     * 
     * @param email Dirección de correo electrónico del usuario.
     */
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
	
    /**
     * Muestra una alerta de error con un mensaje específico.
     * 
     * @param message Mensaje de error que se mostrará en la alerta.
     */
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

    
	/**
     * Inicializa la interfaz gráfica y configura los tooltips para mejorar la usabilidad.
     * 
     * @param location URL de inicialización.
     * @param resources Recursos utilizados en la inicialización.
     */
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		helpBtn.setTooltip(new Tooltip("Ayuda"));
		changePasswordBtn.setTooltip(new Tooltip("Cambiar contraseña"));
		backToLoginBtn.setTooltip(new Tooltip("Volver al login"));
		emailField.setTooltip(new Tooltip("Introduce tu email"));
		passwordField.setTooltip(new Tooltip("Introduce tu nueva contraseña"));
		passwordField2.setTooltip(new Tooltip("Confirma tu nueva contraseña"));
	}
}
