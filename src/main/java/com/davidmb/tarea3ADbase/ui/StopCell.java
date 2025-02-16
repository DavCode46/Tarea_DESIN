package com.davidmb.tarea3ADbase.ui;

import com.davidmb.tarea3ADbase.models.Stop;

import javafx.scene.control.ListCell;

public class StopCell extends ListCell<Stop> {
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