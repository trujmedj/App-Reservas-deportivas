package ProyectoPW.src.dto;

import java.util.Date;

/**
 * Clase que representa una reserva de adultos para una pista deportiva.
 * Esta clase hereda de la clase {@link ReservaDTO} y añade la capacidad
 * de gestionar el número de adultos en la reserva.
 */
public class ReservaAdultosDTO extends ReservaDTO {
    
    private int numAdultos_; // Número de adultos en la reserva

    /**
     * Constructor vacío que inicializa una reserva de adultos sin parámetros.
     */
    public ReservaAdultosDTO() {
    }

    /**
     * Constructor parametrizado para inicializar una reserva de adultos
     * con los datos proporcionados.
     *
     * @param correoUsuario Correo electrónico del usuario
     * @param fechaHora    Fecha y hora de la reserva
     * @param duracion     Duración de la reserva en minutos
     * @param idPista      Identificación de la pista reservada
     * @param precio       Precio de la reserva
     * @param descuento    Descuento aplicado a la reserva
     * @param numAdultos   Número de adultos en la reserva
     * @param bono         Indica si se aplica un bono en la reserva
     */
    public ReservaAdultosDTO(String correoUsuario, Date fechaHora, int duracion, String idPista, float precio, float descuento, int numAdultos, boolean bono) {
        super(correoUsuario, fechaHora, duracion, idPista, precio, descuento, bono);
        this.numAdultos_ = numAdultos;
    }

    // Getters y Setters

    /**
     * Obtiene el número de adultos en la reserva.
     *
     * @return el número de adultos
     */
    public int getNumAdultos() {
        return numAdultos_;
    }

    /**
     * Establece el número de adultos en la reserva.
     *
     * @param numAdultos el nuevo número de adultos
     */
    public void setNumAdultos(int numAdultos) {
        numAdultos_ = numAdultos;
    }

    /**
     * Devuelve una representación en String de la información de la reserva de adultos,
     * incluyendo la información de la reserva base.
     *
     * @return la información de la reserva de adultos en formato String
     */
    @Override
    public String toString() {
        return super.toString() + ", ReservaAdultos{numAdultos=" + numAdultos_ + "}";
    }
}
