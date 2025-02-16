package com.davidmb.tarea3ADbase.ui;

import com.davidmb.tarea3ADbase.models.Service;


import javafx.scene.control.ListCell;

public class ServiceCell extends ListCell<Service>{
	 @Override
	    protected void updateItem(Service service, boolean empty) {
	        super.updateItem(service, empty);
	        if (empty || service == null) {
	            setText(null);
	        } else {
	            setText(service.getServiceName() + " - " + service.getPrice());
	        }
	    }
}
