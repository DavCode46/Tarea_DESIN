package com.davidmb.tarea3ADbase.view;

import java.util.ResourceBundle;

/**
 * Enumeración que representa las diferentes vistas FXML utilizadas en la aplicación.
 * Cada valor enum contiene el título de la vista y la ruta del archivo FXML correspondiente.
 * 
 * Proporciona métodos para obtener el título de la vista y la ruta del archivo FXML.
 * 
 * Uso de `ResourceBundle` para obtener los títulos de las vistas desde un archivo de recursos `Bundle.properties`.
 * 
 * @author DavidMB
 */
public enum FxmlView {
	/**
	 * Vista de administrador.	
	 */
	ADMIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("admin.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Admin.fxml";
		}
	},
	/**
	 * Vista de peregrino.
     */
	PILGRIM {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("pilgrim.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Pilgrim.fxml";
		}
	},
	/**
	 * Vista de parada.
	 */
	STOP {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("stop.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Stop.fxml";
		}
	},
	/**
	 * Vista de recuperación de contraseña.
	 */
	FORGOT_PASSWORD {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("forgotpassword.title");
		}
		
		@Override
		public String getFxmlFile() {
			return "/fxml/ForgotPassword.fxml";
		}
	},
	/**
	 * Vista de inicio
	 */
	LOGIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("login.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Login.fxml";
		}
	},
	/**
	 * Vista de registro
	 */
	REGISTER_PILGRIM {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("registerpilgrim.title");
		}

		@Override
		public String getFxmlFile() {
			return"/fxml/RegisterPilgrim.fxml";
		}
	};

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}
