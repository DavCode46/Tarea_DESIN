package com.davidmb.tarea3ADbase.utils;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase que representa un diálogo de ayuda en la aplicación.
 * 
 * Esta clase extiende `Dialog<Void>` y muestra una ventana modal con contenido
 * de ayuda cargado desde un archivo HTML ubicado en `/help/index.html`.
 * 
 * Se usa un `WebView` para renderizar el contenido de la ayuda, 
 * y se añade un botón de cierre para cerrar el diálogo.
 * 
 * Además, se asigna un icono a la ventana del diálogo.
 * 
 * @author DavidMB
 */
public class HelpDialog extends Dialog<Void> {

    /**
     * Constructor de la clase `HelpDialog`.
     * 
     * Configura un diálogo modal con un `WebView` para mostrar la ayuda en formato HTML.
     * Se carga el archivo `/help/index.html`, se establece el botón de cierre y 
     * se configura un icono para la ventana del diálogo.
     */
    public HelpDialog() {
        // Configurar el diálogo como modal
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Ayuda de la Aplicación");

        // Crear un WebView para mostrar el contenido de ayuda
        WebView webView = new WebView();
        webView.getEngine().load(getClass().getResource("/help/index.html").toExternalForm());

        // Crear el layout del diálogo
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(webView);

        // Configurar el contenido del diálogo
        DialogPane dialogPane = getDialogPane();
        dialogPane.setContent(stackPane);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);

        // Configurar el icono de la ventana del diálogo
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/ask.png")));

        // Configurar el comportamiento del botón "Cerrar"
        setResultConverter(buttonType -> null);
    }
}
