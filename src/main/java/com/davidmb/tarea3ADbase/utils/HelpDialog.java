package com.davidmb.tarea3ADbase.utils;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpDialog extends Dialog<Void> {

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
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/ask.png")));;

        // Configurar el comportamiento del botón "Cerrar"
        setResultConverter(buttonType -> null);
    }
}