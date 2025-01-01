package com.davidmb.tarea3ADbase.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.modelo.User;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
        if (!validateInputs()) {
            return; 
        }

        String email = emailField.getText();
        String newPassword = passwordField.getText();
        User user = userService.findByEmail(email);

        try {
            
            userService.resetPassword(user, newPassword);
           // showSuccessMessage("Contraseña restablecida con éxito. Revisa tu correo.");
        } catch (IllegalArgumentException e) {
            showError(lblEmailError, e.getMessage());
        } catch (Exception e) {
            showError(lblEmailError, "Ocurrió un error inesperado. Inténtalo de nuevo.");
        }
    }


    @FXML
    private void backToLogin() {
		// Cambiar a la vista de "Login"
		stageManager.switchScene(FxmlView.LOGIN);
    }
    
    private boolean validateInputs() {
        boolean isValid = true;

        // Validar email
        if (isNullOrEmpty(emailField.getText())) {
            showError(lblEmailError, "El campo de email no puede estar vacío");
            isValid = false;
        } else if (!emailField.getText().matches(EMAIL_PATTERN)) {
            showError(lblEmailError, "El email no tiene un formato válido");
            isValid = false;
        } else {
            hideError(lblEmailError);
        }

        // Validar contraseña
        if (isNullOrEmpty(passwordField.getText())) {
            showError(lblPasswordError, "El campo de contraseña no puede estar vacío");
            isValid = false;
        } else if (!passwordField.getText().matches(PASSWORD_PATTERN)) {
            showError(lblPasswordError, "La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, una minúscula, un número y un carácter especial");
            isValid = false;
        } else {
            hideError(lblPasswordError);
        }

        // Validar confirmación de contraseña
        if (isNullOrEmpty(passwordField2.getText())) {
            showError(lblPasswordError, "El campo de confirmación de contraseña no puede estar vacío");
            isValid = false;
        } else if (!passwordField.getText().equals(passwordField2.getText())) {
            showError(lblPasswordError, "Las contraseñas no coinciden");
            isValid = false;
        } else {
            hideError(lblPasswordError);
        }

        return isValid;
    }

    private boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
        label.setStyle("-fx-text-fill: red;");
    }

    private void hideError(Label label) {
        label.setVisible(false);
        label.setText("");
    }

    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
