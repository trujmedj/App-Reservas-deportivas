package ProyectoPW.src.dao;

import ProyectoPW.src.common.DBConnection;
import ProyectoPW.src.dto.JugadorDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JugadorDAO {

    private final DBConnection dbConnection = new DBConnection(); // Usamos DBConnection para gestionar la conexión

    /**
     * Verifica si un usuario con las credenciales proporcionadas existe en la base de datos.
     * @param email El correo electrónico del jugador.
     * @param password La contraseña del jugador.
     * @return true si el usuario existe; false si no.
     */
    public boolean verificarUsuario(String email, String password) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.select.verificarUsuario");

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Agrega un nuevo jugador a la base de datos.
     * @param jugador El objeto JugadorDTO con los datos del jugador a agregar.
     * @return true si el jugador fue agregado correctamente; false si ocurrió un error.
     */
    public boolean agregarJugador(JugadorDTO jugador) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.insert.agregarJugador");

        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jugador.getCorreo());
            stmt.setString(2, jugador.getNombre());
            stmt.setString(3, jugador.getApellidos());
            stmt.setDate(4, new java.sql.Date(jugador.getFechaNacimiento().getTime()));
            stmt.setDate(5, new java.sql.Date(jugador.getFechaInscripcion().getTime()));
            stmt.setString(6, jugador.getContrasena());
            stmt.setString(7, jugador.getRol());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar jugador a la base de datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la lista de todos los jugadores registrados en la base de datos.
     * @return Una lista con los jugadores.
     */
    public List<JugadorDTO> listarJugadores() {
        List<JugadorDTO> jugadores = new ArrayList<>();
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.select.listarJugadores");

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellidos");
                Date fechaNacimiento = rs.getDate("fechaNacimiento");
                Date fechaInscripcion = rs.getDate("fechaInscripcion");
                String correo = rs.getString("email");
                String contrasena = rs.getString("contrasena");
                String rol = rs.getString("rol");

                jugadores.add(new JugadorDTO(nombre, apellidos, fechaNacimiento, fechaInscripcion, correo, contrasena, rol));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar jugadores de la base de datos: " + e.getMessage());
        }
        return jugadores;
    }

    /**
     * Modifica los datos de un jugador en la base de datos.
     * @param jugador El objeto JugadorDTO con los nuevos datos del jugador.
     * @param correoAntiguo El correo electrónico antiguo del jugador para identificarlo.
     * @return true si la modificación fue exitosa; false si ocurrió un error.
     */
    public boolean modificarJugador(JugadorDTO jugador, String correoAntiguo) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.update.modificarJugador");

        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jugador.getNombre());
            stmt.setString(2, jugador.getApellidos());
            stmt.setDate(3, new java.sql.Date(jugador.getFechaNacimiento().getTime()));
            stmt.setString(4, jugador.getCorreo());
            stmt.setString(5, jugador.getContrasena());
            stmt.setString(6, jugador.getRol());
            stmt.setString(7, correoAntiguo); // Utilizando el correo antiguo como identificador
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar jugador en la base de datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un jugador en la base de datos utilizando su correo electrónico.
     * @param correo El correo electrónico del jugador.
     * @return Un objeto JugadorDTO con los datos del jugador, o null si no se encuentra.
     */
    public JugadorDTO buscarJugadorPorCorreo(String correo) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.select.buscarJugadorPorCorreo");

        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String apellidos = rs.getString("apellidos");
                    Date fechaNacimiento = rs.getDate("fechaNacimiento");
                    Date fechaInscripcion = rs.getDate("fechaInscripcion");
                    String contrasena = rs.getString("contrasena");
                    String rol = rs.getString("rol");

                    return new JugadorDTO(nombre, apellidos, fechaNacimiento, fechaInscripcion, correo, contrasena, rol);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar jugador por correo en la base de datos: " + e.getMessage());
        }
        return null;
    }
}
