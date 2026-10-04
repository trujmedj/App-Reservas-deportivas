package ProyectoPW.src.dao;

import ProyectoPW.src.common.DBConnection;
import ProyectoPW.src.dto.MaterialDTO;
import ProyectoPW.src.dto.PistaDTO;

import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PistaDAO {

    private DBConnection dbConnection = new DBConnection(); // Usamos DBConnection para gestionar la conexión

    /**
     * Inserta una nueva pista en la base de datos.
     * @param pista La pista a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertarPista(PistaDTO pista) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.insert.insertarPista");
        // String query = "INSERT INTO Pista (nombre, disponible, esExterior, tamano, maxJugadores) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, pista.getNombre());
            stmt.setBoolean(2, pista.isDisponible());
            stmt.setBoolean(3, pista.isEsExterior());
            stmt.setString(4, pista.getTamano().name());
            stmt.setInt(5, pista.getMaxJugadores());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar pista en la base de datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una lista de todas las pistas en la base de datos.
     * @return Una lista con todas las pistas.
     */
    public List<PistaDTO> obtenerTodasLasPistas() {
        List<PistaDTO> pistas = new ArrayList<>();
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.select.obtenerTodasLasPistas");
        // String query = "SELECT * FROM Pista";
        try (Connection conn = dbConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                boolean disponible = rs.getBoolean("disponible");
                boolean esExterior = rs.getBoolean("esExterior");
                PistaDTO.TamanoPista tamano = PistaDTO.TamanoPista.valueOf(rs.getString("tamano"));
                int maxJugadores = rs.getInt("maxJugadores");

                PistaDTO pista = new PistaDTO(nombre, disponible, esExterior, tamano, maxJugadores);

                // Cargar materiales asociados a la pista
                cargarMaterialesDePista(pista);
                pistas.add(pista);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todas las pistas: " + e.getMessage());
        }
        return pistas;
    }

    /**
     * Carga los materiales asociados a una pista.
     * @param pista La pista a la que se desean cargar los materiales.
     */
    private void cargarMaterialesDePista(PistaDTO pista) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.select.cargarMaterialesDePista");
        /*String query = "SELECT m.idMaterial, m.tipo, m.usoExterior, m.estado FROM Material m " +
                       "JOIN Pista_Material pm ON m.idMaterial = pm.idMaterial " +
                       "WHERE pm.nombrePista = ?";*/
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, pista.getNombre());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idMaterial = rs.getInt("idMaterial");
                    MaterialDTO.TipoMaterial tipo = MaterialDTO.TipoMaterial.valueOf(rs.getString("tipo"));
                    boolean usoExterior = rs.getBoolean("usoExterior");
                    MaterialDTO.EstadoMaterial estado = MaterialDTO.EstadoMaterial.valueOf(rs.getString("estado"));

                    MaterialDTO material = new MaterialDTO(idMaterial, tipo, usoExterior, estado);
                    pista.getMateriales().add(material);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar materiales de pista: " + e.getMessage());
        }
    }

    /**
     * Asocia un material a una pista y actualiza el estado del material.
     * @param nombrePista El nombre de la pista.
     * @param idMaterial El ID del material.
     * @return true si se asoció correctamente y se actualizó el estado del material, false en caso contrario.
     */
    public boolean asociarMaterialAPista(String nombrePista, int idMaterial) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String insertQuery = sqlProperties.getProperty("sql.insert.asociarMaterialAPista");
        String updateQuery = sqlProperties.getProperty("sql.update.actualizarEstadoMaterial");
        // String insertQuery = "INSERT INTO Pista_Material (nombrePista, idMaterial) VALUES (?, ?)";
        // String updateQuery = "UPDATE Material SET estado = ? WHERE idMaterial = ?";

        Connection conn = null; // Declarar fuera del try para que sea accesible en el catch

        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar una transacción

            // Asocia el material a la pista
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, nombrePista);
                insertStmt.setInt(2, idMaterial);
                insertStmt.executeUpdate();
            }

            // Actualiza el estado del material
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, "RESERVADO"); // Cambia el estado a RESERVADO
                updateStmt.setInt(2, idMaterial);
                updateStmt.executeUpdate();
            }

            conn.commit(); // Confirmar la transacción
            return true;

        } catch (SQLException e) {
            System.out.println("Error al asociar material a la pista: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Deshacer la transacción en caso de error
                } catch (SQLException rollbackEx) {
                    System.out.println("Error al hacer rollback: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close(); // Cerrar la conexión manualmente en el finally
                } catch (SQLException closeEx) {
                    System.out.println("Error al cerrar la conexión: " + closeEx.getMessage());
                }
            }
        }
    }

    /**
     * Elimina un material de una pista.
     * @param nombrePista El nombre de la pista.
     * @param idMaterial El ID del material.
     * @return true si el material fue eliminado correctamente, false en caso contrario.
     */
    public boolean eliminarMaterialDePista(String nombrePista, int idMaterial) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.delete.eliminarMaterialDePista");
        // String query = "DELETE FROM Pista_Material WHERE nombrePista = ? AND idMaterial = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombrePista);
            stmt.setInt(2, idMaterial);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar material de la pista: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una pista junto con sus materiales.
     * @param nombre El nombre de la pista.
     * @return La pista con sus materiales.
     */
    public PistaDTO obtenerPistaConMateriales(String nombre) {
        PistaDTO pista = null;
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.select.obtenerPistaConMateriales");
        // String query = "SELECT * FROM Pista WHERE nombre = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean disponible = rs.getBoolean("disponible");
                    boolean esExterior = rs.getBoolean("esExterior");
                    PistaDTO.TamanoPista tamano = PistaDTO.TamanoPista.valueOf(rs.getString("tamano"));
                    int maxJugadores = rs.getInt("maxJugadores");

                    pista = new PistaDTO(nombre, disponible, esExterior, tamano, maxJugadores);

                    // Cargar materiales asociados
                    cargarMaterialesDePista(pista);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pista con materiales: " + e.getMessage());
        }
        return pista;
    }

    /**
     * Busca pistas disponibles según el tipo (interior o exterior) y la fecha.
     *
     * @param esExterior Si la pista es exterior o no.
     * @param fecha Fecha para verificar disponibilidad.
     * @return Lista de pistas disponibles que cumplen los criterios.
     */
    public List<PistaDTO> buscarPistasPorTipoYFecha(boolean esExterior, Date fecha) {
        List<PistaDTO> pistas = new ArrayList<>();
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.select.buscarPistasPorTipoYFecha");
        // String query = "SELECT * FROM Pista WHERE esExterior = ? AND disponible = TRUE";

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, esExterior);
            // Configura aquí el parámetro de fecha si es necesario

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    boolean disponible = rs.getBoolean("disponible");
                    boolean esExteriorDB = rs.getBoolean("esExterior");
                    PistaDTO.TamanoPista tamano = PistaDTO.TamanoPista.valueOf(rs.getString("tamano"));
                    int maxJugadores = rs.getInt("maxJugadores");

                    PistaDTO pista = new PistaDTO(nombre, disponible, esExteriorDB, tamano, maxJugadores);
                    cargarMaterialesDePista(pista);
                    pistas.add(pista);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar pistas: " + e.getMessage());
        }
        return pistas;
    }

    /**
     * Modifica el estado de una pista (disponibilidad).
     * @param nombrePista El nombre de la pista.
     * @param disponible El nuevo estado de la pista (disponible o no).
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean modificarEstadoPista(String nombrePista, boolean disponible) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.update.modificarEstadoPista");
        // String query = "UPDATE Pista SET disponible = ? WHERE nombre = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, disponible);
            stmt.setString(2, nombrePista);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar el estado de la pista: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca todas las pistas disponibles en la base de datos.
     *
     * @return Una lista de pistas disponibles.
     */
    public List<PistaDTO> buscarPistasDisponibles() {
        List<PistaDTO> pistasDisponibles = new ArrayList<>();
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.select.buscarPistasDisponibles");
        // String query = "SELECT * FROM Pista WHERE disponible = TRUE";

        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Iterar sobre los resultados de la consulta
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                boolean disponible = rs.getBoolean("disponible");
                boolean esExterior = rs.getBoolean("esExterior");
                PistaDTO.TamanoPista tamano = PistaDTO.TamanoPista.valueOf(rs.getString("tamano"));
                int maxJugadores = rs.getInt("maxJugadores");

                // Crear el objeto PistaDTO y cargar materiales asociados
                PistaDTO pista = new PistaDTO(nombre, disponible, esExterior, tamano, maxJugadores);
                cargarMaterialesDePista(pista);
                
                // Agregar la pista a la lista si está disponible
                pistasDisponibles.add(pista);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar pistas disponibles: " + e.getMessage());
        }
        return pistasDisponibles;
    }
}
