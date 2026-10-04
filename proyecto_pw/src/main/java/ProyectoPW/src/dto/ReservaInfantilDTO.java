package ProyectoPW.src.dto;

import java.util.Date;

/**
 * Clase que representa una reserva infantil.
 * Esta clase extiende la clase {@link ReservaDTO} y agrega información específica
 * sobre el número de niños en la reserva.
 */
public class ReservaInfantilDTO extends ReservaDTO {
    
    private int numNinos_; // Número de niños en la reserva

    /**
     * Constructor vacío para crear una instancia de ReservaInfantil sin inicializar atributos.
     * Este constructor se utiliza cuando se desea crear una reserva infantil sin proporcionar 
     * parámetros iniciales.
     */
    public ReservaInfantilDTO() {
    }

    /**
     * Constructor parametrizado para crear una reserva infantil.
     * Este constructor inicializa una instancia de {@code ReservaInfantilDTO} con los datos 
     * proporcionados, incluidos los detalles sobre el número de niños en la reserva.
     *
     * @param idUsuario     El identificador del usuario que realiza la reserva.
     * @param fechaHora     La fecha y hora de la reserva.
     * @param duracion      La duración de la reserva en minutos.
     * @param idPista       El identificador de la pista reservada.
     * @param precio        El precio de la reserva.
     * @param descuento     El descuento aplicado a la reserva.
     * @param numNinos      El número de niños en la reserva.
     * @param bono          Indica si se aplica un bono en la reserva.
     */
    public ReservaInfantilDTO(String idUsuario, Date fechaHora, int duracion, String idPista, float precio, float descuento, int numNinos, boolean bono) {
        super(idUsuario, fechaHora, duracion, idPista, precio, descuento, bono);
        numNinos_ = numNinos; // Asignar el número de niños
    }

    // Getters y Setters

    /**
     * Obtiene el número de niños en la reserva.
     *
     * @return El número de niños en la reserva.
     */
    public int getNumNinos() {
        return numNinos_;
    }

    /**
     * Establece el número de niños en la reserva.
     *
     * @param numNinos El número de niños a establecer.
     */
    public void setNumNinos(int numNinos) {
        numNinos_ = numNinos;
    }

    /**
     * Método que devuelve una representación en forma de cadena de la reserva infantil.
     * Este método sobrescribe el método {@code toString()} de la clase {@code ReservaDTO} 
     * para incluir detalles sobre el número de niños en la reserva.
     *
     * @return Una cadena que representa la reserva infantil, incluyendo detalles sobre el número de niños.
     */
    @Override
    public String toString() {
        return super.toString() + ", ReservaInfantil{numNinos=" + numNinos_ + "}";
    }
}
