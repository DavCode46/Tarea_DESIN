package com.davidmb.tarea3ADbase.test.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.davidmb.tarea3ADbase.controller.PilgrimController;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.StayService;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.utils.ExportarCarnetXML;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

@ExtendWith(MockitoExtension.class)
public class PilgrimControllerTest {

    @InjectMocks
    private PilgrimController controller;

    // Servicios y demás dependencias inyectadas
    @Mock
    private PilgrimService pilgrimService;
    @Mock
    private ExportarCarnetXML exportarCarnet;
    @Mock
    private StayService stayService;
    @Mock
    private StopService stopService;
    @Mock
    private Session session;
    @Mock
    private UserService userService;
    @Mock
    private StageManager stageManager;

    // Componentes de la interfaz
    @Mock
    private TextField userName;

    @Mock
    private TextField userEmail;
    @Mock
    private PasswordField userPassword;
    @Mock
    private TextField userPasswordVisibleField;
    @Mock
    private PasswordField confirmUserPassword;
    @Mock
    private TextField confirmUserPasswordVisibleField;
    @Mock
    private Button logoutBtn;
    @Mock
    private Button exportCarnetBtn;
    @Mock
    private Button exportReportBtn;
    @Mock
    private Button resetBtn;
    @Mock
    private Button updateUserBtn;
    @Mock
    private Button btnHelp;
    @Mock
    private ImageView avatarImageView;
    @Mock
    private Label lblName;
    @Mock
    private Label lblNationality;
    @Mock
    private Label lblDoExp;
    @Mock
    private Label lblDistance;
    @Mock
    private Label lnlnVips;
    @Mock
    private Label lblInitialStop;

 
    private PilgrimController spyController;
    
//    @BeforeAll
//	static void setUpAll() {
//		// Inicializar JavaFX Toolkit
//		Platform.startup(() -> {
//		});
//	}

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spyController = spy(controller);

       
        spyController.userName = userName;
        spyController.userEmail = userEmail;
        spyController.userPassword = userPassword;
        spyController.userPasswordVisibleField = userPasswordVisibleField;
        spyController.confirmUserPassword = confirmUserPassword;
        spyController.confirmUserPasswordVisibleField = confirmUserPasswordVisibleField;
        spyController.logoutBtn = logoutBtn;
        spyController.exportCarnetBtn = exportCarnetBtn;
        spyController.exportReportBtn = exportReportBtn;
        spyController.resetBtn = resetBtn;
        spyController.updateUserBtn = updateUserBtn;
        spyController.btnHelp = btnHelp;
        spyController.avatarImageView = avatarImageView;
        spyController.lblName = lblName;
        spyController.lblNationality = lblNationality;
        spyController.lblDoExp = lblDoExp;
        spyController.lblDistance = lblDistance;
        spyController.lnlnVips = lnlnVips;
        spyController.lblInitialStop = lblInitialStop;
    }

    @Test
    void testUpdateUserSuccessUsingHiddenPassword() {
  
        doReturn(true).when(spyController).validateData();
        doReturn(true).when(spyController).showConfirmAlert(any(User.class), any(Pilgrim.class));

        
        doNothing().when(spyController).saveAlert(any(Pilgrim.class), any(User.class));
        doNothing().when(spyController).loadUserProfile(any(User.class));
      

        
        
        User sessionUser = new User("NombreAntiguo", "Peregrino", "antiguo@example.com", "Antiguo@123");
        sessionUser.setId(1L);
        when(session.getLoggedInUser()).thenReturn(sessionUser);

        Pilgrim pilgrim = new Pilgrim("NombreAntiguo", "NacionalidadAntigua", null, null);
        when(pilgrimService.findByUserId(1L)).thenReturn(pilgrim);

      
        when(userEmail.getText()).thenReturn("nuevo@example.com");
      
        when(userPassword.isVisible()).thenReturn(true);
        when(userPassword.getText()).thenReturn("Nuevo4646@");
   

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            spyController.updateUser();
            latch.countDown();
        });
   
        try {
			assertTrue(latch.await(5, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        assertEquals("nuevo@example.com", sessionUser.getEmail());
        assertEquals("Nuevo4646@", sessionUser.getPassword());
        verify(userService, times(1)).save(sessionUser);

     
        verify(userEmail, times(1)).clear();
    }

    @Test
    void testUpdateUserSuccessUsingVisiblePassword() {
    
        doReturn(true).when(spyController).validateData();
        doReturn(true).when(spyController).showConfirmAlert(any(User.class), any(Pilgrim.class));
        doNothing().when(spyController).saveAlert(any(Pilgrim.class), any(User.class));
        doNothing().when(spyController).loadUserProfile(any(User.class));
       

   
        User sessionUser = new User("NombreAntiguo", "Peregrino", "antiguo@example.com", "Antiguo@123");
        sessionUser.setId(2L);
        when(session.getLoggedInUser()).thenReturn(sessionUser);

        Pilgrim pilgrim = new Pilgrim("NombreAntiguo", "NacionalidadAntigua", null, null);
        when(pilgrimService.findByUserId(2L)).thenReturn(pilgrim);

    
        when(userEmail.getText()).thenReturn("cambiado@example.com");
        when(userPassword.isVisible()).thenReturn(false);
        when(userPasswordVisibleField.getText()).thenReturn("Nuevo4646@");
     

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            spyController.updateUser();
            latch.countDown();
        });

        try {
			assertTrue(latch.await(5, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        assertEquals("cambiado@example.com", sessionUser.getEmail());
        assertEquals("Nuevo4646@", sessionUser.getPassword());
        verify(userService, times(1)).save(sessionUser);
        verify(userEmail, times(1)).clear();
    }

    @Test
    void testUpdateUserCancelled() {
        
        doReturn(true).when(spyController).validateData();
        doReturn(false).when(spyController).showConfirmAlert(any(User.class), any(Pilgrim.class));

        
   
        doNothing().when(spyController).showInfoAlert(anyString(), anyString(), anyString());
       

        
       
        User sessionUser = new User("Nombre", "Peregrino", "email@example.com", "Usuario4646@");
        sessionUser.setId(3L);
        when(session.getLoggedInUser()).thenReturn(sessionUser);

        Pilgrim pilgrim = new Pilgrim("Nombre", "Nacionalidad", null, null);
        when(pilgrimService.findByUserId(3L)).thenReturn(pilgrim);

        // Simular valores de entrada
        when(userEmail.getText()).thenReturn("cambiado@example.com");
        when(userPassword.isVisible()).thenReturn(true);
        when(userPassword.getText()).thenReturn("Nuevo4646@");
  


        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            spyController.updateUser();
            latch.countDown();
        });

        try {
			assertTrue(latch.await(5, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        verify(userService, never()).save(any());

        verify(userEmail, never()).clear();
    }

    @Test
    void testUpdateUserInvalidData() {
    
    	doNothing().when(spyController).showErrorAlert(anyString(), anyString());

   
        doReturn(false).when(spyController).validateData();

   
        spyController.updateUser();

    
        verify(session, never()).getLoggedInUser();
        verify(userService, never()).save(any());
    }
}
