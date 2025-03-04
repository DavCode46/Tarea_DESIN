package com.davidmb.tarea3ADbase.test.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.controller.LoginController;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @InjectMocks
    private LoginController controller;


    @Mock
    private Button btnLogin;
    @Mock
    private Button btnRegisterPilgrim;
    @Mock
    private Button btnForgotPassword;
    @Mock
    private Button btnHelp;
    @Mock
    private PasswordField password;
    @Mock
    private TextField username;
    @Mock
    private CheckBox showPasswordCheckBox;
    @Mock
    private TextField passwordVisibleField;
    @Mock
    private Label lblLogin;

 
    @Mock
    private UserService userService;
    @Mock
    private StageManager stageManager;
    @Mock
    private Session session;

    private LoginController spyController;

//    @BeforeAll
//    static void setUpAll() {
//        // Inicializar el Toolkit de JavaFX una sola vez para los tests.
//        Platform.startup(() -> { });
//    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spyController = spy(controller);

        spyController.btnLogin = btnLogin;
        spyController.btnRegisterPilgrim = btnRegisterPilgrim;
        spyController.btnForgotPassword = btnForgotPassword;
        spyController.btnHelp = btnHelp;
        spyController.password = password;
        spyController.username = username;
        spyController.showPasswordCheckBox = showPasswordCheckBox;
        spyController.passwordVisibleField = passwordVisibleField;
        spyController.lblLogin = lblLogin;
    }

    /**
     * Test: Inicio de sesión exitoso usando el campo de contraseña predeterminado.
     */
    @Test
    void testLoginSuccessUsingDefaultPasswordField() throws IOException, InterruptedException {
    
        when(username.getText()).thenReturn("test@example.com");
        when(passwordVisibleField.isVisible()).thenReturn(false);
        when(password.getText()).thenReturn("pass1234");
        

        when(userService.authenticate("test@example.com", "pass1234")).thenReturn(true);
 
        User user = new User("Test User", "PEREGRINO", "test@example.com", "pass1234");
        user.setId(1L);
        when(userService.findByEmail("test@example.com")).thenReturn(user);
        
       
        doNothing().when(spyController).showSuccessAlert(user);
        doNothing().when(spyController).clearFields();
        
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                spyController.login(new ActionEvent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        
       
        verify(session, times(1)).setLoggedInUser(user);
      
        verify(stageManager, times(1)).switchScene(FxmlView.PILGRIM);
       
        verify(spyController, times(1)).clearFields();
    }

    /**
     * Test: Inicio de sesión exitoso usando el campo de contraseña visible.
     */
    @Test
    void testLoginSuccessUsingVisiblePasswordField() throws IOException, InterruptedException {
     
        when(username.getText()).thenReturn("visible@example.com");
        when(passwordVisibleField.isVisible()).thenReturn(true);
        when(passwordVisibleField.getText()).thenReturn("visiblepass");
    

      
        when(userService.authenticate("visible@example.com", "visiblepass")).thenReturn(true);
      
        User user = new User("Visible User", "ADMIN", "visible@example.com", "visiblepass");
        user.setId(2L);
        when(userService.findByEmail("visible@example.com")).thenReturn(user);
        
        doNothing().when(spyController).showSuccessAlert(user);
        doNothing().when(spyController).clearFields();

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                spyController.login(new ActionEvent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        
       
        verify(session, times(1)).setLoggedInUser(user);
    
        verify(stageManager, times(1)).switchScene(FxmlView.ADMIN);
   
        verify(spyController, times(1)).clearFields();
    }

    /**
     * Test: Inicio de sesión fallido (autenticación incorrecta).
     */
    @Test
    void testLoginFailure() throws IOException, InterruptedException {
    
        when(username.getText()).thenReturn("wrong@example.com");
        when(passwordVisibleField.isVisible()).thenReturn(false);
        when(password.getText()).thenReturn("wrongpass");

     
        when(userService.authenticate("wrong@example.com", "wrongpass")).thenReturn(false);
        
   
        doNothing().when(spyController).showErrorAlert();
        
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                spyController.login(new ActionEvent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        
    
        verify(stageManager, never()).switchScene(any());
        verify(session, never()).setLoggedInUser(any());
    }
}
