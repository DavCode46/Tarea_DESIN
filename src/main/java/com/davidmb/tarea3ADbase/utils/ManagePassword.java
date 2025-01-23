package com.davidmb.tarea3ADbase.utils;

import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ManagePassword {

	public static void showPassword(TextField passwordVisibleField, PasswordField password,
			 CheckBox showPasswordCheckBox,TextField confirmPasswordVisibleField, PasswordField confirmPassword) {

		if (showPasswordCheckBox.isSelected()) {
			// Mostrar la contraseña en el TextField
			passwordVisibleField.setText(password.getText());
			passwordVisibleField.setVisible(true);
			passwordVisibleField.setManaged(true);
			password.setVisible(false);
			password.setManaged(false);
			if (confirmPasswordVisibleField != null && confirmPassword != null) {
				confirmPasswordVisibleField.setText(confirmPassword.getText());
				confirmPasswordVisibleField.setVisible(true);
				confirmPasswordVisibleField.setManaged(true);
				confirmPassword.setVisible(false);
				confirmPassword.setManaged(false);
			}

		} else {
			// Ocultar la contraseña en el PasswordField
			password.setText(passwordVisibleField.getText());
			password.setVisible(true);
			password.setManaged(true);
			passwordVisibleField.setVisible(false);
			passwordVisibleField.setManaged(false);
			
			if (confirmPasswordVisibleField != null && confirmPassword != null) {
				confirmPassword.setText(confirmPasswordVisibleField.getText());
				confirmPassword.setVisible(true);
				confirmPassword.setManaged(true);
				confirmPasswordVisibleField.setVisible(false);
				confirmPasswordVisibleField.setManaged(false);
			}
		}
		
	}
}
