package com.davidmb.tarea3ADbase.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.controller.RegisterPilgrimController;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.PilgrimStops;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.PilgrimStopsService;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@ExtendWith(MockitoExtension.class)
public class RegisterPilgrimControllerTest {

    @InjectMocks
    private RegisterPilgrimController controller;

    @Mock
    private StageManager stageManager;

    @Mock
    private UserService userService;

    @Mock
    private PilgrimService pilgrimService;

    @Mock
    private StopService stopService;

    @Mock
    private PilgrimStopsService pilgrimStopsService;

    @Mock
    private TextField nameField;

    @Mock
    private TextField emailField;

    @Mock
    private PasswordField passwordField;

    @Mock
    private TextField passwordVisibleField;

    @Mock
    private PasswordField confirmPasswordField;

    @Mock
    private TextField confirmPasswordVisibleField;

    @Mock
    private ComboBox<String> nationalityComboBox;

    @Mock
    private ComboBox<Stop> stopComboBox;

    @Mock
    private Label errorLabel;

    @Mock
    private Button registerBtn;

    @Mock
    private Button helpBtn;

    @Mock
    private Button returnBtn;

    private RegisterPilgrimController spyController;
    
//    @BeforeAll
//    public static void setupJavaFX() {
//        Platform.startup(() -> {});
//    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        spyController = spy(controller);

 
        spyController.nameField = nameField;
        spyController.emailField = emailField;
        spyController.passwordField = passwordField;
        spyController.passwordVisibleField = passwordVisibleField;
        spyController.confirmPasswordField = confirmPasswordField;
        spyController.confirmPasswordVisibleField = confirmPasswordVisibleField;
        spyController.nationalityComboBox = nationalityComboBox;
        spyController.stopComboBox = stopComboBox;
        spyController.errorLabel = errorLabel;
        spyController.registerBtn = registerBtn;
        spyController.helpBtn = helpBtn;
        spyController.returnBtn = returnBtn;
    }

    @Test
    void testRegisterPilgrimSuccessUsingHiddenPassword() {
      
        doReturn(true).when(spyController).validateData();
        doReturn(true).when(spyController).confirmAlert(any(User.class), any(Pilgrim.class));
        doNothing().when(spyController).showInfoAlert(any(User.class));

        when(nameField.getText()).thenReturn("Juan");
        when(emailField.getText()).thenReturn("juan@example.com");
        when(nationalityComboBox.getValue()).thenReturn("Española");

 
        Stop stop = new Stop();
        stop.setId(1L);
        when(stopComboBox.getValue()).thenReturn(stop);


        when(passwordVisibleField.isVisible()).thenReturn(false);
        when(passwordField.getText()).thenReturn("Usuario4646@");

      
        Stop foundStop = new Stop();
        foundStop.setId(1L);
  
        when(stopService.find(1L)).thenReturn(foundStop);

 
        User savedUser = new User("Juan", "Peregrino", "juan@example.com", "Usuario4646@");
        savedUser.setId(100L);
        when(userService.save(any(User.class))).thenReturn(savedUser);

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            spyController.registerPilgrim();
            latch.countDown();
        });
        try {
			assertTrue(latch.await(5, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        verify(userService, times(1)).save(any(User.class));
        verify(pilgrimService, times(1)).save(any(Pilgrim.class));
        verify(pilgrimStopsService, times(1)).save(any(PilgrimStops.class));
        verify(stageManager, times(1)).switchScene(FxmlView.LOGIN);
    }

    @Test
    void testRegisterPilgrimSuccessUsingVisiblePassword() {
        doReturn(true).when(spyController).validateData();
        doReturn(true).when(spyController).confirmAlert(any(User.class), any(Pilgrim.class));
        doNothing().when(spyController).showInfoAlert(any(User.class));

        when(nameField.getText()).thenReturn("Maria");
        when(emailField.getText()).thenReturn("maria@example.com");
        when(nationalityComboBox.getValue()).thenReturn("Mexicana");

        Stop stop = new Stop();
        stop.setId(2L);
        when(stopComboBox.getValue()).thenReturn(stop);

        when(passwordVisibleField.isVisible()).thenReturn(true);
        when(passwordVisibleField.getText()).thenReturn("Usuario4646@");
     

        Stop foundStop = new Stop();
        foundStop.setId(2L);
        when(stopService.find(2L)).thenReturn(foundStop);

        User savedUser = new User("Maria", "Peregrino", "maria@example.com", "Usuario4646@");
        savedUser.setId(101L);
        when(userService.save(any(User.class))).thenReturn(savedUser);

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            spyController.registerPilgrim();
            latch.countDown();
        });
        try {
			assertTrue(latch.await(5, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


     
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).save(userCaptor.capture());
        User userSaved = userCaptor.getValue();
        assertEquals("Usuario4646@", userSaved.getPassword());

        verify(pilgrimService, times(1)).save(any(Pilgrim.class));
        verify(pilgrimStopsService, times(1)).save(any(PilgrimStops.class));
        verify(stageManager, times(1)).switchScene(FxmlView.LOGIN);
    }

    @Test
    void testRegisterPilgrimNotConfirmed() {
  
        doReturn(true).when(spyController).validateData();
        doReturn(false).when(spyController).confirmAlert(any(User.class), any(Pilgrim.class));
        doNothing().when(spyController).showInfoAlert((User) null);

        when(nameField.getText()).thenReturn("Juan");
        when(emailField.getText()).thenReturn("juan@example.com");
        when(nationalityComboBox.getValue()).thenReturn("Española");

        Stop stop = new Stop();
        stop.setId(1L);
        when(stopComboBox.getValue()).thenReturn(stop);

        when(passwordVisibleField.isVisible()).thenReturn(false);
        when(passwordField.getText()).thenReturn("Usuario4646@");

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            spyController.registerPilgrim();
            latch.countDown();
        });
        try {
			assertTrue(latch.await(5, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


     
        verify(userService, never()).save(any(User.class));
        verify(pilgrimService, never()).save(any(Pilgrim.class));
        verify(pilgrimStopsService, never()).save(any(PilgrimStops.class));
        verify(stageManager, never()).switchScene(any());
    }

    @Test
    void testRegisterPilgrimInvalidData() {

        doReturn(false).when(spyController).validateData();

  
        spyController.registerPilgrim();

        verify(userService, never()).save(any(User.class));
        verify(pilgrimService, never()).save(any(Pilgrim.class));
        verify(pilgrimStopsService, never()).save(any(PilgrimStops.class));
        verify(stageManager, never()).switchScene(any());
    }
}
