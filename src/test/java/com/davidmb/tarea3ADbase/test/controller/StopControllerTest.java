package com.davidmb.tarea3ADbase.test.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.davidmb.tarea3ADbase.controller.StopController;
import com.davidmb.tarea3ADbase.dtos.ServiceResponse;
import com.davidmb.tarea3ADbase.dtos.StayView;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.PilgrimStopsService;
import com.davidmb.tarea3ADbase.services.StopService;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableView;

@ExtendWith(MockitoExtension.class)
public class StopControllerTest {

    @InjectMocks
    private StopController stopController;

    @Mock
    private PilgrimService pilgrimService;

    @Mock
    private StopService stopService;

    @Mock
    private PilgrimStopsService pilgrimStopsService;

    @Mock
    private ComboBox<String> cbPilgrims;

    @Mock
    private CheckBox cbStay;

    @Mock
    private RadioButton rbYes;

    @Mock
    private RadioButton rbNo;

    @Mock
    private TableView<StayView> pilgrimsTable;

    @Mock
    private User user;

    @Mock
    private Label stopId;

    @Mock
    private SingleSelectionModel<String> selectionModel;

    // Usaremos un spy para anular métodos que muestran Alert y evitan bloquear el test
    private StopController spyController;
//
//    @BeforeAll
//    public static void setupJavaFX() {
//        Platform.startup(() -> {});
//    }


	@BeforeEach
    public void setUp() {
     
        spyController = spy(stopController);
      
    }

    @SuppressWarnings("unchecked")
	@Test
    public void testStampCard_Success() {
       
        when(user.getId()).thenReturn(1L);
   
        when(cbPilgrims.getValue()).thenReturn("Peregrino 1");
        when(cbStay.isSelected()).thenReturn(false);
        when(rbYes.isSelected()).thenReturn(true);
        doNothing().when(spyController).saveAlert(any(ServiceResponse.class), any(Pilgrim.class));

        doNothing().when(spyController).clearFields();
        doNothing().when(spyController).loadStayViews();

       
        Pilgrim pilgrim = new Pilgrim("Peregrino", "Español", null, null);
        pilgrim.setId(1L);
     
        when(pilgrimService.find(anyLong())).thenReturn(pilgrim);

       
        Stop stop = new Stop("Parada1", "Reg", "Responsable");
        stop.setId(100L);
        when(stopService.findByUserId(anyLong())).thenReturn(stop);

       
        when(pilgrimStopsService.existsByPilgrimAndStopAndStopDate(any(Pilgrim.class), any(Stop.class), any(LocalDate.class)))
                .thenReturn(false);

      
        ServiceResponse<Pilgrim> serviceResponse = new ServiceResponse<>(true, pilgrim, "Carnet sellado exitosamente");
        when(pilgrimService.stampCard(any(Pilgrim.class), any(Stop.class), anyBoolean(), anyBoolean()))
                .thenReturn(serviceResponse);

        // Act
        spyController.stampCard(new ActionEvent());

      
        verify(pilgrimService, times(1)).stampCard(any(Pilgrim.class), any(Stop.class), anyBoolean(), anyBoolean());
        verify(pilgrimStopsService, times(1)).existsByPilgrimAndStopAndStopDate(any(Pilgrim.class), any(Stop.class),
                any(LocalDate.class));
    }


	@Test
    public void testStampCard_PilgrimAlreadyStamped() {
        doNothing().when(spyController).showErrorAlert(any(StringBuilder.class), any(String.class));
      
        Pilgrim pilgrim = new Pilgrim("Peregrino", "Español", null, null);
        pilgrim.setId(123L);

        Stop stop = new Stop("Parada1", "Reg", "Responsable");
        stop.setId(1L);

        when(cbPilgrims.getValue()).thenReturn("Peregrino 123");
        when(user.getId()).thenReturn(1L);
        when(pilgrimService.find(anyLong())).thenReturn(pilgrim);
        when(stopService.findByUserId(anyLong())).thenReturn(stop);

        // Simula que ya existe un sello
        when(pilgrimStopsService.existsByPilgrimAndStopAndStopDate(any(Pilgrim.class), any(Stop.class), any(LocalDate.class)))
                .thenReturn(true);

        // Act
        spyController.stampCard(new ActionEvent());

   
        verify(pilgrimService, never()).stampCard(any(Pilgrim.class), any(Stop.class), anyBoolean(), anyBoolean());
    }

    @SuppressWarnings("unchecked")
	@Test
    public void testStampCard_ServiceResponseNotSuccess() {
       
        when(user.getId()).thenReturn(1L);
     
        when(cbPilgrims.getValue()).thenReturn("Peregrino 1");
        when(cbStay.isSelected()).thenReturn(false);
        when(rbYes.isSelected()).thenReturn(true);
   
        doNothing().when(spyController).showServiceResponseError(any(ServiceResponse.class), any(Pilgrim.class));
       
        
        doNothing().when(spyController).clearFields();
        doNothing().when(spyController).loadStayViews();

        Pilgrim pilgrim = new Pilgrim("Peregrino", "Español", null, null);
        pilgrim.setId(123L);
        when(pilgrimService.find(anyLong())).thenReturn(pilgrim);

        Stop stop = new Stop("Parada1", "Reg", "Responsable");
        stop.setId(100L);
        when(stopService.findByUserId(anyLong())).thenReturn(stop);

        when(pilgrimStopsService.existsByPilgrimAndStopAndStopDate(any(Pilgrim.class), any(Stop.class), any(LocalDate.class)))
                .thenReturn(false);

        ServiceResponse<Pilgrim> serviceResponse = new ServiceResponse<>(false, pilgrim, "Error al sellar el carnet");
        when(pilgrimService.stampCard(any(Pilgrim.class), any(Stop.class), anyBoolean(), anyBoolean()))
                .thenReturn(serviceResponse);

        // Act
        spyController.stampCard(new ActionEvent());

        // Assert
        verify(pilgrimService, times(1)).stampCard(any(Pilgrim.class), any(Stop.class), anyBoolean(), anyBoolean());
     
    }
}
