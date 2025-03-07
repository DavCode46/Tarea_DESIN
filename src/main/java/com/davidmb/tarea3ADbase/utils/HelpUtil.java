package com.davidmb.tarea3ADbase.utils;

import javafx.fxml.FXML;

/**
 * Utilidad para mostrar la ayuda de la aplicación.
 * 
 * Esta clase proporciona un método estático para abrir el diálogo de ayuda (`HelpDialog`).
 * Puede ser llamada desde cualquier parte de la aplicación para mostrar la ventana de ayuda.
 * 
 * @author DavidMB
 */
public class HelpUtil {

    /**
     * Muestra el diálogo de ayuda de la aplicación.
     * 
     * Este método crea una instancia de `HelpDialog` y la muestra como una ventana modal.
     * 
     * Se utiliza la anotación `@FXML` para que pueda ser invocado desde archivos FXML 
     * en caso de ser necesario.
     */
    @FXML
    public static void showHelp() {
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.showAndWait();
    }
}
