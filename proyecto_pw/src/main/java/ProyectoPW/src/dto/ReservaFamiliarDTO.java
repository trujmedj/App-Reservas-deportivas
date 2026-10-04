package ProyectoPW.src.dto;

import java.util.Date;

/**
 * Clase que representa una reserva familiar.
 * Esta clase extiende la clase {@link ReservaDTO} y agrega información específica
 * sobre el número de adultos y niños en la reserva.
 */
public class ReservaFamiliarDTO extends ReservaDTO {
    
    private int numAdultos_; // Número de adultos en la reserva
    private int numNinos_;   // Número de niños en la reserva

    /**
     * Constructor parametrizado para crear una reserva familiar.
     * Este constructor inicializa una instancia de la clase {@code ReservaFamiliarDTO} 
     * con los datos específicos de la reserva, incluyendo el número de adultos y niños.
     *
     * @param correoUsuario  El correo electrónico del usuario que realiza la reserva.
     * @param fechaHora     La fecha y hora de la reserva.
     * @param duracion      La duración de la reserva en minutos.
     * @param idPista       El identificador de la pista reservada.
     * @param precio        El precio de la reserva.
     * @param descuento     El descuento aplicado a la reserva.
     * @param numAdultos    El número de adultos en la reserva.
     * @param numNinos      El número de niños en la reserva.
     * @param bono          Indica si se aplica un bono en la reserva.
     */
    public ReservaFamiliarDTO(String correoUsuario, Date fechaHora, int duracion, String idPista, float precio, float descuento, int numAdultos, int numNinos, boolean bono) {
        super(correoUsuario, fechaHora, duracion, idPista, precio, descuento, bono);
        numAdultos_ = numAdultos; // Asignar el número de adultos
        numNinos_ = numNinos;     // Asignar el número de niños
    }

    // Getters y Setters

    /**
     * Obtiene el número de adultos en la reserva.
     *
     * @return El número de adultos en la reserva.
     */
    public int getNumAdultos() {
        return numAdultos_;
    }

    /**
     * Establece el número de adultos en la reserva.
     *
     * @param numAdultos El número de adultos a establecer.
     */
    public void setNumAdultos(int numAdultos) {
        numAdultos_ = numAdultos;
    }

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
     * Método que devuelve una representación en forma de cadena de la reserva familiar.
     * Incluye detalles de la reserva base (proporcionada por la clase {@code ReservaDTO}),
     * así como el número de adultos y niños.
     *
     * @return Una cadena que representa la reserva familiar, incluyendo detalles de adultos y niños.
     */
    @Override
    public String toString() {
        return super.toString() + ", ReservaFamiliar{numAdultos=" + numAdultos_ + ", numNinos=" + numNinos_ + "}";
    }
}
