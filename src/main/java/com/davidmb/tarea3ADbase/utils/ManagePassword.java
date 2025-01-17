package com.davidmb.tarea3ADbase.utils;

import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ManagePassword {

	public static void showPassword(TextField passwordVisibleField, PasswordField password, CheckBox showPasswordCheckBox) {
		if (showPasswordCheckBox.isSelected()) {
        // Mostrar la contraseña en el TextField
	        passwordVisibleField.setText(password.getText());
	        passwordVisibleField.setVisible(true);
	        passwordVisibleField.setManaged(true);
	        password.setVisible(false);
	        password.setManaged(false);
    	
		} else {
        // Ocultar la contraseña en el PasswordField
	        password.setText(passwordVisibleField.getText());
	        password.setVisible(true);
	        password.setManaged(true);
	        passwordVisibleField.setVisible(false);
	        passwordVisibleField.setManaged(false);
		}
    }
}
