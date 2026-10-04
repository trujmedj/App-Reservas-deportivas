package ProyectoPW.src.dto;

import java.util.Date;

/**
 * Clase que representa un bono de reservas para un jugador.
 * Un bono tiene un identificador, información sobre el jugador, fechas de creación y caducidad, 
 * un número de sesiones restantes y el tipo de reserva que se puede realizar con él.
 */
public class BonoDTO {

    private int idBono_; // Identificador del bono
    private String correoJugador_; // Correo del jugador asociado al bono
    private Date fechaCreacion_; // Fecha de creación del bono
    private Date fechaCaducidad_; // Fecha de caducidad del bono
    private int sesionesRestantes_; // Número de sesiones restantes en el bono
    private int tipoReserva_; // Tipo de reserva: 1 (Infantil), 2 (Familiar), 3 (Adultos)

    /**
     * Constructor para crear un nuevo bono.
     *
     * @param idBono        El identificador del bono.
     * @param correoJugador  El correo electrónico del jugador que posee el bono.
     * @param fechaCreacion  La fecha en que se creó el bono.
     * @param tipoReserva    El tipo de reserva asociado al bono (1: Infantil, 2: Familiar, 3: Adultos).
     */
    public BonoDTO(int idBono, String correoJugador, Date fechaCreacion, int tipoReserva) {
        idBono_ = idBono; // Asigna el idBono
        correoJugador_ = correoJugador; // Asigna el correo del jugador
        fechaCreacion_ = fechaCreacion; // Asigna la fecha de creación
        // Establece la fecha de caducidad a un año a partir de la fecha de creación
        fechaCaducidad_ = new Date(fechaCreacion.getTime() + (365L * 24 * 60 * 60 * 1000)); 
        sesionesRestantes_ = 5; // Inicializa el número de sesiones restantes a 5
        tipoReserva_ = tipoReserva; // Asigna el tipo de reserva
    }

    // Métodos Getters

    /**
     * Obtiene el identificador del bono.
     *
     * @return El id del bono.
     */
    public int getIdBono() {
        return idBono_;
    }

    /**
     * Establece el identificador del bono.
     *
     * @param idBono El nuevo identificador del bono.
     */
    public void setIdBono(int idBono) {
        idBono_ = idBono;
    }

    /**
     * Obtiene el correo del jugador asociado al bono.
     *
     * @return El correo del jugador.
     */
    public String getCorreoJugador() {
        return correoJugador_;
    }

    /**
     * Obtiene la fecha de creación del bono.
     *
     * @return La fecha de creación.
     */
    public Date getFechaCreacion() {
        return fechaCreacion_;
    }

    /**
     * Obtiene la fecha de caducidad del bono.
     *
     * @return La fecha de caducidad.
     */
    public Date getFechaCaducidad() {
        return fechaCaducidad_;
    }

    /**
     * Establece la fecha de caducidad del bono.
     *
     * @param fechaCaducidad La nueva fecha de caducidad.
     */
    public void setFechaCaducidad(Date fechaCaducidad) {
        fechaCaducidad_ = fechaCaducidad;
    }

    /**
     * Obtiene el número de sesiones restantes en el bono.
     *
     * @return El número de sesiones restantes.
     */
    public int getSesionesRestantes() {
        return sesionesRestantes_;
    }

    /**
     * Establece el número de sesiones restantes en el bono.
     *
     * @param sesionesRestantes El nuevo número de sesiones restantes.
     */
    public void setSesionesRestantes(int sesionesRestantes) {
        sesionesRestantes_ = sesionesRestantes;
    }

    /**
     * Obtiene el tipo de reserva asociado al bono.
     *
     * @return El tipo de reserva (1, 2 o 3).
     */
    public int getTipoReserva() {
        return tipoReserva_;
    }

    /**
     * Usa una sesión del bono, reduciendo el número de sesiones restantes.
     * No realiza ninguna acción si no hay sesiones restantes.
     */
    public void usarSesion() {
        if (sesionesRestantes_ > 0) {
            sesionesRestantes_--; // Decrementa el contador de sesiones restantes
        }
    }

    /**
     * Devuelve una representación en forma de cadena del bono.
     * 
     * Esta representación incluye el identificador del bono, el correo del jugador, las fechas de creación 
     * y caducidad, el número de sesiones restantes y el tipo de reserva asociado.
     *
     * @return Una cadena que representa el bono.
     */
    @Override
    public String toString() {
        return "Bono{" +
                "idBono='" + idBono_ + '\'' +
                ", jugador=" + correoJugador_ +
                ", fechaCreacion=" + fechaCreacion_ +
                ", fechaCaducidad=" + fechaCaducidad_ +
                ", sesionesRestantes=" + sesionesRestantes_ +
                ", tipoReserva=" + tipoReserva_ +
                '}';
    }
}



