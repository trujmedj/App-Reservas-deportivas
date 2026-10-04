package ProyectoPW.src.dto;

import java.util.Date;
import java.util.Calendar;

/**
 * Clase que representa un jugador con información personal como nombre,
 * apellidos, fecha de nacimiento, fecha de inscripción, correo electrónico,
 * contraseña y rol.
 */
public class JugadorDTO {
    // Atributos del jugador
    private String nombre_; // Nombre del jugador
    private String apellidos_; // Apellidos del jugador
    private Date fechaNacimiento_; // Fecha de nacimiento del jugador
    private Date fechaInscripcion_; // Fecha de inscripción del jugador
    private String correo_; // Correo electrónico del jugador
    private String contrasena_; // Contraseña encriptada del jugador
    private String rol_; // Rol del jugador (e.g., 'jugador', 'admin')

    /**
     * Constructor por defecto que inicializa un nuevo objeto Jugador.
     */
    public JugadorDTO() {
    }

    /**
     * Constructor parametrizado para inicializar un objeto Jugador con
     * los datos proporcionados.
     *
     * @param nombre          Nombre del jugador
     * @param apellidos       Apellidos del jugador
     * @param fechaNacimiento Fecha de nacimiento del jugador
     * @param fechaInscripcion Fecha de inscripción del jugador
     * @param correo          Correo electrónico del jugador
     * @param contrasena        Contraseña encriptada del jugador
     * @param rol            Rol del jugador (e.g., 'jugador', 'admin')
     */
    public JugadorDTO(String nombre, String apellidos, Date fechaNacimiento, Date fechaInscripcion, String correo, String contrasena, String rol) {
        nombre_ = nombre;
        apellidos_ = apellidos;
        fechaNacimiento_ = fechaNacimiento;
        fechaInscripcion_ = fechaInscripcion;
        correo_ = correo;
        contrasena_ = contrasena;
        rol_ = rol;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return el nombre del jugador
     */
    public String getNombre() {
        return nombre_;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param nombre el nuevo nombre del jugador
     */
    public void setNombre(String nombre) {
        nombre_ = nombre;
    }

    /**
     * Obtiene los apellidos del jugador.
     *
     * @return los apellidos del jugador
     */
    public String getApellidos() {
        return apellidos_;
    }

    /**
     * Establece los apellidos del jugador.
     *
     * @param apellidos los nuevos apellidos del jugador
     */
    public void setApellidos(String apellidos) {
        apellidos_ = apellidos;
    }

    /**
     * Obtiene la fecha de nacimiento del jugador en formato Date.
     *
     * @return la fecha de nacimiento del jugador
     */
    public Date getFechaNacimiento() {
        return fechaNacimiento_;
    }

    /**
     * Establece la fecha de nacimiento del jugador.
     *
     * @param fechaNacimiento la nueva fecha de nacimiento del jugador
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        fechaNacimiento_ = fechaNacimiento;
    }

    /**
     * Obtiene la fecha de inscripción del jugador en formato Date.
     *
     * @return la fecha de inscripción del jugador
     */
    public Date getFechaInscripcion() {
        return fechaInscripcion_;
    }

    /**
     * Establece la fecha de inscripción del jugador.
     *
     * @param fechaInscripcion la nueva fecha de inscripción del jugador
     */
    public void setFechaInscripcion(Date fechaInscripcion) {
        fechaInscripcion_ = fechaInscripcion;
    }

    /**
     * Obtiene el correo electrónico del jugador.
     *
     * @return el correo electrónico del jugador
     */
    public String getCorreo() {
        return correo_;
    }

    /**
     * Establece el correo electrónico del jugador.
     *
     * @param correo el nuevo correo electrónico del jugador
     */
    public void setCorreo(String correo) {
        correo_ = correo;
    }

    /**
     * Obtiene la contraseña del jugador.
     *
     * @return la contraseña del jugador
     */
    public String getContrasena() {
        return contrasena_;
    }

    /**
     * Establece la contraseña del jugador.
     *
     * @param contrasena la nueva contraseña del jugador
     */
    public void setContrasena(String contrasena) {
        contrasena_ = contrasena;
    }

    /**
     * Obtiene el rol del jugador.
     *
     * @return el rol del jugador (e.g., 'jugador', 'admin')
     */
    public String getRol() {
        return rol_;
    }

    /**
     * Establece el rol del jugador.
     *
     * @param rol el nuevo rol del jugador (e.g., 'jugador', 'admin')
     */
    public void setRol(String rol) {
        rol_ = rol;
    }

    /**
     * Devuelve una representación en String de la información del jugador.
     *
     * @return la información del jugador en formato String
     */
    @Override
    public String toString() {
        return "Informacion usuario{" +
                "NOMBRE: " + nombre_ +
                ", APELLIDOS: " + apellidos_ +
                ", FECHA NACIMIENTO: " + getFechaNacimiento() +
                ", FECHA INSCRIPCION: " + getFechaInscripcion() +
                ", CORREO: " + correo_ +
                ", ROL: " + rol_ + 
                '}';
    }

    /**
     * Calcula la antigüedad del jugador en años desde su fecha de inscripción.
     *
     * @return la antigüedad en años, o 0 si la fecha de inscripción no está definida
     */
    public int calcularAntiguedad() {
        if (fechaInscripcion_ == null) {
            return 0; // Retorna 0 si la fecha de inscripción no está definida
        }

        // Obtener la fecha actual
        Calendar fechaActual = Calendar.getInstance();

        // Crear un calendario para la fecha de inscripción
        Calendar fechaInscripcionCal = Calendar.getInstance();
        fechaInscripcionCal.setTime(fechaInscripcion_);

        // Calcular la diferencia en años
        int años = fechaActual.get(Calendar.YEAR) - fechaInscripcionCal.get(Calendar.YEAR);

        // Ajustar si el mes/día de inscripción es posterior al actual en el año
        if (fechaActual.get(Calendar.DAY_OF_YEAR) < fechaInscripcionCal.get(Calendar.DAY_OF_YEAR)) {
            años--;
        }

        return años;
    }
}
