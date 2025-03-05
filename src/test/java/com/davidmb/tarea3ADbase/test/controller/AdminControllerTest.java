package com.davidmb.tarea3ADbase.test.controller;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.controller.AdminController;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.utils.ExportarCarnetXML;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;

import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;


@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @InjectMocks
    private AdminController controller;


    @Mock
    private StopService stopService;
    @Mock
    private UserService userService;
    @Mock
    private PilgrimService pilgrimService;
    @Mock
    private ExportarCarnetXML exportarCarnet;
    @Mock
    private Session session;
    @Mock
    private StageManager stageManager;

  
    @Mock
    private TextField stopName;
    @Mock
    private ComboBox<String> cbregion;
    @Mock
    private TextField managerName;
    @Mock
    private TextField managerEmail;
    @Mock
    private PasswordField managerPassword;
    @Mock
    private TextField managerPasswordVisibleField;
    @Mock
    private PasswordField confirmManagerPassword;
    @Mock
    private TextField confirmManagerPasswordVisibleField;
    @Mock
    private CheckBox showPasswordCheckBox;
    @Mock
    private Button saveStop;
 
    @Mock
    private Button btnLogout;
    @Mock
    private Button btnHelp;
    @Mock
    private Button exportStopsBtn;
    @Mock
    private Button btnAddServices;
    @Mock
    private Label loggedInUser;
  
    @Mock
    private SingleSelectionModel<String> selectionModel;

    private AdminController spyController;

    @BeforeAll
    static void setUpAll() {
  
        Platform.startup(() -> {
        });
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spyController = spy(controller);

    
        spyController.stopName = stopName;
        spyController.cbregion = cbregion;
        spyController.managerName = managerName;
        spyController.managerEmail = managerEmail;
        spyController.managerPassword = managerPassword;
        spyController.managerPasswordVisibleField = managerPasswordVisibleField;
        spyController.confirmManagerPassword = confirmManagerPassword;
        spyController.confirmManagerPasswordVisibleField = confirmManagerPasswordVisibleField;
        spyController.showPasswordCheckBox = showPasswordCheckBox;
        spyController.saveStop = saveStop;
        spyController.btnLogout = btnLogout;
        spyController.btnHelp = btnHelp;
        spyController.exportStopsBtn = exportStopsBtn;
        spyController.btnAddServices = btnAddServices;
        spyController.loggedInUser = loggedInUser;
    }

    /**
     * Test: Registro exitoso de una parada (usando contraseña oculta).
     */
    @Test
    void testSaveStopSuccess() throws InterruptedException {
     
        doReturn(true).when(spyController).validateData();
       
        doReturn(true).when(spyController).showConfirmAlert(any(User.class), any(Stop.class));
        doNothing().when(spyController).saveAlert(any(Stop.class));
 
        doNothing().when(spyController).clearFields();
        doNothing().when(spyController).loadStopDetails();
        

   
        when(stopName.getText()).thenReturn("Stop1");
      
        when(cbregion.getSelectionModel()).thenReturn(selectionModel);
        when(selectionModel.getSelectedItem()).thenReturn("RegionName");
        when(managerName.getText()).thenReturn("Manager1");
        when(managerEmail.getText()).thenReturn("manager@example.com");
  
        when(managerPassword.getText()).thenReturn("Secret@123");
      

        User newUser = new User();
        newUser.setUsername("Manager1");
        newUser.setEmail("manager@example.com");
        newUser.setPassword("Secret@123");
        newUser.setRole("Parada");
        newUser.setId(10L);
        when(userService.save(any(User.class))).thenReturn(newUser);

       
        when(stopService.existsByNameAndRegion("Stop1", "Reg")).thenReturn(false);
        
      
        Stop newStop = new Stop("Stop1", "Reg", "Manager1");
        newStop.setUserId(10L);
        when(stopService.save(any(Stop.class))).thenReturn(newStop);

      
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            spyController.saveStop(new ActionEvent());
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));

        verify(userService, times(1)).save(any(User.class));
        verify(stopService, times(1)).save(any(Stop.class));
      
        verify(spyController, times(1)).clearFields();
        verify(spyController, times(1)).loadStopDetails();
      
        verify(spyController, times(1)).saveAlert(newStop);
    }

    /**
     * Test: Registro cancelado (confirmación devuelve false).
     */
    @Test
    void testSaveStopCancelled() throws InterruptedException {
  
        doReturn(true).when(spyController).validateData();
        doReturn(false).when(spyController).showConfirmAlert(any(User.class), any(Stop.class));
        doNothing().when(spyController).saveAlert((Stop) null);

  
        when(stopName.getText()).thenReturn("Stop1");
        when(cbregion.getSelectionModel()).thenReturn(selectionModel);
        when(selectionModel.getSelectedItem()).thenReturn("RegionName");
        when(managerName.getText()).thenReturn("Manager1");
        when(managerEmail.getText()).thenReturn("manager@example.com");
        when(managerPassword.getText()).thenReturn("Secret@123");

      
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            spyController.saveStop(new ActionEvent());
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));

        verify(userService, never()).save(any(User.class));
        verify(stopService, never()).save(any(Stop.class));
        verify(spyController, times(1)).saveAlert((Stop) null);
        verify(spyController, never()).clearFields();
    }

    /**
     * Test: Registro duplicado (la parada ya existe).
     */
    @Test
    void testSaveStopDuplicate() throws InterruptedException {
        doReturn(true).when(spyController).validateData();
        doReturn(true).when(spyController).showConfirmAlert(any(User.class), any(Stop.class));
        doNothing().when(spyController).showErrorAlert(any(StringBuilder.class));

        when(stopName.getText()).thenReturn("Stop1");
        when(cbregion.getSelectionModel()).thenReturn(selectionModel);
        when(selectionModel.getSelectedItem()).thenReturn("RegionName");
        when(managerName.getText()).thenReturn("Manager1");
        when(managerEmail.getText()).thenReturn("manager@example.com");
 
        when(managerPassword.getText()).thenReturn("Secret@123");


        User newUser = new User();
        newUser.setUsername("Manager1");
        newUser.setEmail("manager@example.com");
        newUser.setPassword("Secret@123");
        newUser.setRole("Parada");
        newUser.setId(10L);
        when(userService.save(any(User.class))).thenReturn(newUser);

        when(stopService.existsByNameAndRegion("Stop1", "Reg")).thenReturn(true);

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            spyController.saveStop(new ActionEvent());
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));

        verify(userService, times(1)).save(any(User.class));
        verify(stopService, never()).save(any(Stop.class));
        verify(spyController, never()).saveAlert(any(Stop.class));
        verify(spyController, times(1)).showErrorAlert(any(StringBuilder.class));
        verify(spyController, never()).clearFields();
        verify(spyController, never()).loadStopDetails();
    }
}
