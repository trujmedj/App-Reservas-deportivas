package ProyectoPW.src.manager;

import ProyectoPW.src.dto.JugadorDTO;
import ProyectoPW.src.dto.PistaDTO;
import ProyectoPW.src.dto.ReservaDTO;
import ProyectoPW.src.factory.*;
import ProyectoPW.src.dto.BonoDTO;
import ProyectoPW.src.dao.ReservaDAO;
import ProyectoPW.src.dao.BonoDAO;
import java.util.*;

/**
 * Clase que gestiona las reservas de pistas y bonos.
 * Permite realizar reservas individuales y con bonos, así como 
 * modificar y cancelar reservas existentes.
 */
public class GestorReservas {
    
    // DAO para gestionar reservas
    private ReservaDAO reservaDAO;
    
    // DAO para gestionar bonos
    private BonoDAO bonoDAO;

    /**
     * Constructor de la clase GestorReservas.
     * Inicializa los DAOs para las reservas y los bonos.
     */
    public GestorReservas() {
        reservaDAO = new ReservaDAO(); // Crear instancia del DAO de reservas
        bonoDAO = new BonoDAO(); // Crear instancia del DAO de bonos
    }

    /**
     * Realiza una reserva individual para un jugador.
     *
     * @param jugador   El jugador que realiza la reserva.
     * @param fechaHora La fecha y hora de la reserva.
     * @param duracion  La duración de la reserva en minutos.
     * @param pista     La pista que se reserva.
     * @param numAdultos Número de adultos en la reserva.
     * @param numNinos   Número de niños en la reserva.
     * @return La reserva creada.
     */
    public ReservaDTO hacerReservaIndividual(JugadorDTO jugador, Date fechaHora, int duracion, PistaDTO pista, int numAdultos, int numNinos) {
        // Llama a la ReservaIndividualFactory para crear la reserva
        ReservaDTO reserva = ReservaIndividualFactory.crearReserva(jugador, fechaHora, duracion, pista, numAdultos, numNinos);

        if (reserva != null) {
            // Agregar la reserva al DAO
            reservaDAO.hacerReservaIndividual(reserva);
        }

        return reserva; // Retorna la reserva creada
    }

    /**
     * Realiza una reserva utilizando un bono.
     *
     * @param jugador       El jugador que realiza la reserva.
     * @param fechaHora     La fecha y hora de la reserva.
     * @param duracion      La duración de la reserva en minutos.
     * @param pista         La pista que se reserva.
     * @param bono          El bono utilizado para la reserva.
     * @param numAdultos    Número de adultos en la reserva.
     * @param numNinos      Número de niños en la reserva.
     * @return La reserva creada, o null si no se pudo realizar.
     */
    public ReservaDTO hacerReservaBono(JugadorDTO jugador, Date fechaHora, int duracion, PistaDTO pista, BonoDTO bono, int numAdultos, int numNinos) {
        // Llamar a la función crearReserva de ReservaBonoFactory para crear la reserva con bono
        ReservaDTO reserva = ReservaBonoFactory.crearReserva(jugador, fechaHora, duracion, pista, numAdultos, numNinos, bono);

        // Si la reserva fue exitosa, retornar la reserva
        if (reserva != null) {
            // Intentar realizar la reserva a través del DAO
            boolean exito = bonoDAO.hacerReservaBono(bono, reserva);

            // Si la reserva fue exitosa, retornar la reserva
            if (exito) {
                return reserva;
            } else {
                return null; // Si no se pudo hacer la reserva, retornar null
            }
        } else {
            return null;
        }
    }

    /**
     * Modifica una reserva existente.
     *
     * @param reserva        La reserva a modificar.
     * @param nuevaFechaHora La nueva fecha y hora para la reserva.
     * @param nuevaDuracion  La nueva duración de la reserva en minutos.
     * @return True si la modificación fue exitosa, false en caso contrario.
     */
    public boolean modificarReservaMini(ReservaDTO reserva, Date nuevaFechaHora, int nuevaDuracion) {
        if (reservaDAO.modificarReservaMini(reserva, nuevaFechaHora, nuevaDuracion)) {
            return true; // Modificación exitosa
        }
        return false; // No se pudo modificar
    }

    /**
     * Cancela una reserva existente.
     *
     * @param reserva La reserva a cancelar.
     * @return True si la cancelación fue exitosa, false en caso contrario.
     */
    public boolean cancelarReserva(ReservaDTO reserva) {
        if (reservaDAO.cancelarReserva(reserva)) {
            return true; // Cancelación exitosa
        }
        return false; // No se pudo cancelar
    }

    /**
     * Crea un bono para un jugador.
     *
     * @param correoJugador El correo del jugador.
     * @param tipoReserva   El tipo de bono (1: Infantil, 2: Familiar, 3: Adultos).
     * @return El bono creado, o null si no se pudo crear.
     */
    public BonoDTO crearBono(String correoJugador, int tipoReserva) {
        // Obtenemos la fecha de creación actual
        Date fechaCreacion = new Date(); // La fecha actual
        
        // Crear un nuevo bono con la fecha de creación y tipo de reserva
        BonoDTO bono = new BonoDTO(0, correoJugador, fechaCreacion, tipoReserva);

        // Intentar crear el bono a través del DAO
        BonoDTO bonoInsertado = bonoDAO.crearBono(bono);

        // Si la creación fue exitosa, retornamos el bono, de lo contrario, retornamos null
        if (bonoInsertado != null) {
            return bono;
        } else {
            System.out.println("No se pudo crear el bono.");
        }

        return null; // Si no se pudo crear el bono, retornar null
    }

    /**
     * Cuenta el número de reservas futuras a partir de la fecha actual.
     *
     * @return El número de reservas futuras.
     */
    public int contarReservasFuturas() {
        int reservasFuturas = reservaDAO.contarReservasFuturas();
        
        return reservasFuturas;
    }

    /**
     * Lista las reservas para una fecha específica y pista dada.
     *
     * @param fecha La fecha de las reservas que se desean listar.
     * @param pista La pista para la cual se desean listar las reservas.
     * @return Una lista de objetos ReservaDTO que representan las reservas en la fecha y pista especificadas.
     */
    public List<ReservaDTO> listarReservasPorFechaYPista(Date fecha, PistaDTO pista) {
        List<ReservaDTO> reservas = reservaDAO.listarReservasPorFechaYPista(fecha, pista);

        return reservas;
    }

    /**
     * Busca un bono activo asociado a un jugador específico por su correo electrónico.
     *
     * @param correoJugador El correo electrónico del jugador.
     * @return Un objeto BonoDTO si se encuentra el bono activo; null en caso contrario.
     */
    public BonoDTO buscarBonoPorJugador(String correoJugador) {
        BonoDTO bono = bonoDAO.buscarBonoPorJugador(correoJugador);
        
        return bono;
    }

    /**
     * Obtiene una lista de bonos asociados a un jugador dado por su correo electrónico.
     *
     * @param correoJugador El correo electrónico del jugador.
     * @return Una lista de objetos BonoDTO asociados al jugador.
     */
    public List<BonoDTO> obtenerBonosPorJugador(String correoJugador) {
        return bonoDAO.obtenerBonosPorJugador(correoJugador);
    }

    /**
     * Busca la reserva más reciente de un jugador según su correo.
     *
     * @param correoJugador El correo electrónico del jugador.
     * @return Un objeto ReservaDTO si se encuentra una reserva; null en caso contrario.
     */
    public ReservaDTO encontrarReservaPorJugador(String correoJugador) {
        ReservaDTO reserva = reservaDAO.encontrarReservaPorJugador(correoJugador);
        
        return reserva;
    }

    /**
     * Busca un bono específico de un jugador por correo electrónico y tipo de bono.
     *
     * @param correoJugador El correo electrónico del jugador.
     * @param tipoBono      El tipo de bono a buscar (1: Infantil, 2: Familiar, 3: Adultos).
     * @return Un objeto BonoDTO si se encuentra un bono coincidente; null en caso contrario.
     */
    public BonoDTO buscarBonoPorJugadorYTipo(String correoJugador, int tipoBono) {
        // Llama al método correspondiente del BonoDAO para buscar el bono
        BonoDTO bono = bonoDAO.buscarBonoPorJugadorYTipo(correoJugador, tipoBono);
    
        // Retorna el bono encontrado o null si no existe
        return bono;
    }
}
