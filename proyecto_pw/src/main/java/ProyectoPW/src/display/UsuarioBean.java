package ProyectoPW.src.display;

import java.io.Serializable;
import ProyectoPW.src.dto.JugadorDTO;

/**
 * Bean para gestionar la información del jugador durante la sesión.
 * Esta clase contiene los datos del jugador que están siendo gestionados en el contexto de la sesión actual.
 * Además proporciona métodos para obtener información del jugador y verificar su rol.
 */
public class UsuarioBean implements Serializable {

    private JugadorDTO jugador; // Objeto que contiene los datos del jugador.

    /**
     * Constructor por defecto que inicializa el jugador a null.
     */
    public UsuarioBean() {
        jugador = null;
    }

    /**
     * Obtiene el objeto JugadorDTO que contiene los detalles del jugador.
     * 
     * @return El objeto JugadorDTO que representa al jugador.
     */
    public JugadorDTO getJugador() {
        return jugador;
    }

    /**
     * Establece el objeto JugadorDTO que representa al jugador en la sesión.
     * 
     * @param jugador El objeto JugadorDTO que se asignará al jugador en la sesión.
     */
    public void setJugador(JugadorDTO jugador) {
        this.jugador = jugador;
    }

    /**
     * Obtiene el correo del jugador.
     * 
     * @return El correo electrónico del jugador si está disponible, o una cadena vacía si el jugador es null.
     */
    public String getCorreo() {
        if (jugador != null) {
            return jugador.getCorreo();
        }
        return "";
    }

    /**
     * Obtiene el nombre del jugador.
     * 
     * @return El nombre del jugador si está disponible, o una cadena vacía si el jugador es null.
     */
    public String getNombre() {
        if (jugador != null) {
            return jugador.getNombre();
        }
        return "";
    }

    /**
     * Obtiene el rol del jugador (por ejemplo, 'jugador', 'admin').
     * 
     * @return El rol del jugador si está disponible, o una cadena vacía si el jugador es null.
     */
    public String getRol() {
        if (jugador != null) {
            return jugador.getRol();
        }
        return "";
    }

    /**
     * Verifica si el jugador tiene el rol de administrador.
     * 
     * @return true si el jugador tiene el rol de "admin", false en caso contrario.
     */
    public boolean esAdministrador() {
        return "admin".equalsIgnoreCase(getRol());
    }

    /**
     * Verifica si el jugador tiene el rol de cliente/jugador.
     * 
     * @return true si el jugador tiene el rol de "jugador", false en caso contrario.
     */
    public boolean esJugador() {
        return "jugador".equalsIgnoreCase(getRol());
    }
}


