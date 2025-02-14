package com.davidmb.tarea3ADbase.utils;

import javafx.fxml.FXML;

public class HelpUtil {

	@FXML
    public static void showHelp() {
    	
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.showAndWait();
    }
}