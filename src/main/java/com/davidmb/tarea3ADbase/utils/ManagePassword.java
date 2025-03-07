package com.davidmb.tarea3ADbase.utils;

import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Utilidad para gestionar la visualización de contraseñas en formularios.
 * 
 * Esta clase permite alternar la visibilidad de un campo de contraseña (`PasswordField`)
 * mostrando su contenido en un `TextField` cuando se activa un `CheckBox`.
 * 
 * Se admite tanto el campo de contraseña principal como uno opcional para confirmar la contraseña.
 * 
 * @author DavidMB
 */
public class ManagePassword {

    /**
     * Alterna la visibilidad de los campos de contraseña en función del estado del `CheckBox`.
     * 
     * Cuando el `CheckBox` está seleccionado, la contraseña se muestra en un `TextField` visible.
     * Cuando el `CheckBox` está desactivado, la contraseña se oculta en un `PasswordField`.
     * 
     * @param passwordVisibleField Campo de texto donde se mostrará la contraseña en texto plano.
     * @param password Campo de contraseña enmascarado (`PasswordField`).
     * @param showPasswordCheckBox CheckBox que determina si se muestra o no la contraseña.
     * @param confirmPasswordVisibleField Campo opcional donde se mostrará la confirmación de contraseña en texto plano.
     * @param confirmPassword Campo opcional de confirmación de contraseña enmascarado (`PasswordField`).
     */
    public static void showPassword(TextField passwordVisibleField, PasswordField password,
                                    CheckBox showPasswordCheckBox, TextField confirmPasswordVisibleField, 
                                    PasswordField confirmPassword) {

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
