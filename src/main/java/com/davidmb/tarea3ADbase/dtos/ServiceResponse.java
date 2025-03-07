package com.davidmb.tarea3ADbase.dtos;

/**
 * Clase genérica que representa la respuesta de un servicio.
 * 
 * Esta clase encapsula el resultado de una operación realizada por un servicio,
 * proporcionando información sobre el éxito o fallo de la operación, los datos devueltos
 * y un mensaje descriptivo.
 * 
 * Se usa para estandarizar las respuestas en los servicios y facilitar el manejo de resultados.
 * 
 * @param <T> Tipo de dato que se devuelve en la respuesta.
 * 
 * @author DavidMB
 */
public class ServiceResponse<T> {

    /** Indica si la operación fue exitosa (`true`) o fallida (`false`). */
    private boolean success;

    /** Datos devueltos por la operación, si corresponde. */
    private T data;

    /** Mensaje descriptivo sobre el resultado de la operación. */
    private String message;

    /**
     * Constructor de la clase `ServiceResponse`.
     * 
     * @param success Indica si la operación fue exitosa.
     * @param data Datos devueltos por la operación (puede ser `null`).
     * @param message Mensaje descriptivo de la respuesta.
     */
    public ServiceResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    /**
     * Obtiene el estado de la operación.
     * 
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Establece el estado de la operación.
     * 
     * @param success `true` si la operación fue exitosa, `false` en caso contrario.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Obtiene los datos devueltos por la operación.
     * 
     * @return Los datos de la respuesta, o `null` si no hay datos.
     */
    public T getData() {
        return data;
    }

    /**
     * Establece los datos devueltos por la operación.
     * 
     * @param data Datos a establecer en la respuesta.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Obtiene el mensaje descriptivo de la respuesta.
     * 
     * @return Mensaje de la respuesta.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje descriptivo de la respuesta.
     * 
     * @param message Mensaje a establecer en la respuesta.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
