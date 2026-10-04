package ProyectoPW.src.manager;

import ProyectoPW.src.dao.JugadorDAO;
import ProyectoPW.src.dto.JugadorDTO;
import java.util.List;
import java.util.Date;

/**
 * Clase que gestiona las operaciones relacionadas con los jugadores, como agregar, modificar, listar y buscar usuarios.
 * Utiliza un DAO para interactuar con la base de datos o almacenamiento de jugadores.
 */
public class GestorUsuarios {

    // DAO para gestionar los jugadores
    private JugadorDAO jugadorDAO;

    /**
     * Constructor de la clase GestorUsuarios.
     * Inicializa el DAO para gestionar jugadores.
     */
    public GestorUsuarios() {
        this.jugadorDAO = new JugadorDAO(); // Crear instancia del DAO de jugadores
    }

    /**
     * Agrega un nuevo jugador al sistema.
     *
     * @param jugador El objeto JugadorDTO que contiene los datos del jugador a agregar.
     * @return True si el jugador se agrega correctamente; false en caso contrario.
     */
    public boolean agregarJugador(JugadorDTO jugador) {
        return jugadorDAO.agregarJugador(jugador); // Llamada al DAO para agregar al jugador
    }

    /**
     * Modifica la información de un jugador existente.
     *
     * @param nuevoNombre      El nuevo nombre del jugador.
     * @param nuevoApellido    El nuevo apellido del jugador.
     * @param nuevaFechaNacimiento La nueva fecha de nacimiento del jugador.
     * @param nuevoCorreo      El nuevo correo del jugador.
     * @param correoAntiguo    El correo antiguo para identificar al jugador a modificar.
     * @return True si la modificación fue exitosa; false en caso contrario.
     */
    public boolean modificarUsuario(String nuevoNombre, String nuevoApellido, Date nuevaFechaNacimiento, String nuevoCorreo, String correoAntiguo) {
        // Buscar al jugador por su correo antiguo
        JugadorDTO jugador = jugadorDAO.buscarJugadorPorCorreo(correoAntiguo);
        if (jugador != null) {
            // Modificar los datos del jugador
            jugador.setNombre(nuevoNombre);
            jugador.setApellidos(nuevoApellido);
            jugador.setFechaNacimiento(nuevaFechaNacimiento);
            jugador.setCorreo(nuevoCorreo);
            // Llamar al DAO para realizar la modificación
            return jugadorDAO.modificarJugador(jugador, correoAntiguo);
        } else {
            System.out.println("Usuario no encontrado con correo: " + correoAntiguo);
            return false; // Si no se encontró al jugador, retornar false
        }
    }

    /**
     * Lista todos los jugadores registrados en el sistema.
     * Si no hay jugadores registrados, muestra un mensaje indicando que no hay usuarios.
     */
    public void listarUsuarios() {
        // Obtener la lista de jugadores desde el DAO
        List<JugadorDTO> jugadores = jugadorDAO.listarJugadores();
        if (jugadores.isEmpty()) {
            System.out.println("No hay usuarios registrados."); // Si la lista está vacía
        } else {
            System.out.println("Usuarios registrados:");
            for (JugadorDTO jugador : jugadores) {
                // Imprimir los detalles de cada jugador
                System.out.println(jugador);
            }
        }
    }

    /**
     * Busca un jugador en el sistema por su correo electrónico.
     *
     * @param correo El correo electrónico del jugador que se busca.
     * @return El objeto JugadorDTO correspondiente al jugador encontrado, o null si no se encuentra.
     */
    public JugadorDTO buscarUsuarioPorCorreo(String correo) {
        return jugadorDAO.buscarJugadorPorCorreo(correo); // Llamada al DAO para buscar por correo
    }
}
