package com.davidmb.tarea3ADbase.ui;

import com.davidmb.tarea3ADbase.models.Stop;
import javafx.scene.control.ListCell;

/**
 * Representa una celda personalizada para mostrar información de una parada (`Stop`)
 * dentro de un componente `ListView` en JavaFX.
 * 
 * Extiende `ListCell<Stop>` para definir cómo se debe renderizar cada elemento en la lista.
 * 
 * @author DavidMB
 */
public class StopCell extends ListCell<Stop> {

    /**
     * Actualiza la celda con la información de la parada (`Stop`).
     * 
     * Si la celda está vacía o el objeto `Stop` es nulo, no se muestra texto.
     * En caso contrario, se muestra el nombre de la parada junto con su región.
     * 
     * @param stop Objeto `Stop` a mostrar en la celda.
     * @param empty Indica si la celda está vacía o no.
     */
    @Override
    protected void updateItem(Stop stop, boolean empty) {
        super.updateItem(stop, empty);
        if (empty || stop == null) {
            setText(null);
        } else {
            setText(stop.getName() + " - " + stop.getRegion());
        }
    }
}
