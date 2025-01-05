package com.davidmb.tarea3ADbase.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

@Controller
public class PilgrimController implements Initializable {

    @FXML
    private Label lblName;

    @FXML
    private Label lblNationality;

    @FXML
    private Label lblDoExp;

    @FXML
    private Label lblDistance;

    @FXML
    private Label lnlnVips;

    @FXML
    private Label lblInitialStop;

    @FXML
    private ImageView profileImage;

    @Autowired
    private UserService userService;
    
    @Lazy
	@Autowired
	private StageManager stageManager;

    // Usuario actual --> IdUsuario en tabla peregrinos
    private User currentUser;
    private Pilgrim currentPilgrim;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
        loadUserProfile(); // Recibe ID de usuario
    }

    /**
     * Cargar los datos del usuario y actualizarlos en la vista
     */
    private void loadUserProfile() {
        
     //   currentUser = userService.find();
        

        if (currentUser != null) {
            

           
          
        } else {
            showErrorAlert("Error", "El usuario no existe.");
        }
    }

    /**
     * Exportar los datos del carnet
     */
    @FXML
    private void exportCarnet() {
       
    }
    
    
    
    @FXML
	private void logout(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("¿Estás seguro que quieres cerrar sesión?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			stageManager.switchScene(FxmlView.LOGIN);
		}
	}

    /**
     * Alerta de info
     */
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Alerta de error
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
