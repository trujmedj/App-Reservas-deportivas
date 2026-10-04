package ProyectoPW.src.dto;

import java.util.Date;

/**
 * Clase que representa una reserva para una pista deportiva.
 * Contiene información sobre el usuario, fecha y hora de la reserva,
 * duración, identificación de la pista, precio y descuento aplicado.
 */
public class ReservaDTO {
    // Atributos
    private String correoUsuario_; // Correo electrónico del usuario que realiza la reserva
    private Date fechaHora_;        // Fecha y hora de la reserva
    private int duracion_;          // Duración de la reserva en minutos
    private String idPista_;        // Identificación de la pista reservada
    private float precio_;          // Precio de la reserva
    private float descuento_;       // Descuento aplicado a la reserva
    private boolean bono_;          // Indica si se aplica un bono en la reserva

    /**
     * Constructor vacío que inicializa una reserva sin parámetros.
     * Este constructor permite crear una instancia de la clase `ReservaDTO` 
     * sin ninguna información inicial, para luego poder establecer los 
     * valores de sus atributos utilizando los métodos correspondientes.
     */
    public ReservaDTO() {
        
    }

    /**
     * Constructor parametrizado para inicializar una reserva con los datos 
     * proporcionados.
     *
     * @param correoUsuario Correo electrónico del usuario
     * @param fechaHora    Fecha y hora de la reserva
     * @param duracion     Duración de la reserva en minutos
     * @param idPista      Identificación de la pista reservada
     * @param precio       Precio de la reserva
     * @param descuento    Descuento aplicado a la reserva
     * @param bono         Indica si se aplica un bono en la reserva
     */
    public ReservaDTO(String correoUsuario, Date fechaHora, int duracion, String idPista, float precio, float descuento, boolean bono) {
        correoUsuario_ = correoUsuario;
        fechaHora_ = fechaHora;
        duracion_ = duracion;
        idPista_ = idPista;
        precio_ = precio;
        descuento_ = descuento;
        bono_ = bono;
    }

    // Getters y Setters

    /**
     * Obtiene el correo electrónico del usuario que realizó la reserva.
     *
     * @return el correo electrónico del usuario
     */
    public String getCorreoUsuario() {
        return correoUsuario_;
    }

    /**
     * Establece el correo electrónico del usuario que realiza la reserva.
     *
     * @param correoUsuario el nuevo correo electrónico del usuario
     */
    public void setCorreoUsuario(String correoUsuario) {
        correoUsuario_ = correoUsuario;
    }

    /**
     * Obtiene la fecha y hora de la reserva.
     *
     * @return la fecha y hora de la reserva
     */
    public Date getFechaHora() {
        return fechaHora_;
    }

    /**
     * Establece la fecha y hora de la reserva.
     *
     * @param fechaHora la nueva fecha y hora de la reserva
     */
    public void setFechaHora(Date fechaHora) {
        fechaHora_ = fechaHora;
    }

    /**
     * Obtiene la duración de la reserva en minutos.
     *
     * @return la duración de la reserva
     */
    public int getDuracion() {
        return duracion_;
    }

    /**
     * Establece la duración de la reserva en minutos.
     *
     * @param duracion la nueva duración de la reserva
     */
    public void setDuracion(int duracion) {
        duracion_ = duracion;
    }

    /**
     * Obtiene la identificación de la pista reservada.
     *
     * @return la identificación de la pista
     */
    public String getIdPista() {
        return idPista_;
    }

    /**
     * Establece la identificación de la pista reservada.
     *
     * @param idPista la nueva identificación de la pista
     */
    public void setIdPista(String idPista) {
        idPista_ = idPista;
    }

    /**
     * Obtiene el precio de la reserva.
     *
     * @return el precio de la reserva
     */
    public float getPrecio() {
        return precio_;
    }

    /**
     * Establece el precio de la reserva.
     *
     * @param precio el nuevo precio de la reserva
     */
    public void setPrecio(float precio) {
        precio_ = precio;
    }

    /**
     * Obtiene el descuento aplicado a la reserva.
     *
     * @return el descuento de la reserva
     */
    public float getDescuento() {
        return descuento_;
    }

    /**
     * Establece el descuento aplicado a la reserva.
     *
     * @param descuento el nuevo descuento de la reserva
     */
    public void setDescuento(float descuento) {
        descuento_ = descuento;
    }

    /**
     * Obtiene el valor que indica si se aplica un bono en la reserva.
     *
     * @return el valor que indica si se aplica un bono (true/false)
     */
    public boolean getBono() {
        return bono_;
    }

    /**
     * Establece si se aplica un bono en la reserva.
     *
     * @param bono el valor que indica si se aplica un bono (true/false)
     */
    public void setBono(boolean bono) {
        bono_ = bono;
    }

    /**
     * Devuelve una representación en String de la información de la reserva.
     * Incluye todos los atributos de la reserva como el correo del usuario, 
     * la fecha y hora de la reserva, la duración, la identificación de la pista, 
     * el precio, el descuento y el bono.
     *
     * @return la información de la reserva en formato String
     */
    @Override
    public String toString() {
        return "Reserva{" +
                "CORREOUSUARIO: " + correoUsuario_ +
                ", FECHAHORA: " + fechaHora_ +
                ", DURACION: " + duracion_ +
                ", IDPISTA: " + idPista_ +
                ", PRECIO: " + precio_ +
                ", DESCUENTO: " + descuento_ +
                ", BONO: " + bono_ +
                '}';
    }
}
