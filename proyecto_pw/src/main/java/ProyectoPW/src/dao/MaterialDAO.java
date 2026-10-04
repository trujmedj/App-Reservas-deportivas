package ProyectoPW.src.dao;

import ProyectoPW.src.common.DBConnection;
import ProyectoPW.src.dto.MaterialDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MaterialDAO {

    private final DBConnection dbConnection = new DBConnection(); // Usamos DBConnection para gestionar la conexión

    /**
     * Inserta un nuevo material en la base de datos.
     * @param material El material a insertar.
     * @return El objeto MaterialDTO con el ID generado, o null si ocurrió un error.
     */
    public MaterialDTO insertarMaterial(MaterialDTO material) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.insert.insertarMaterial");

        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, material.getIdMaterial());
            stmt.setString(2, material.getTipo().name());
            stmt.setBoolean(3, material.isUsoExterior());
            stmt.setString(4, material.getEstado().name());
            stmt.executeUpdate();

            // Obtener el ID generado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    material.setIdMaterial(idGenerado); // Actualizamos el ID en el objeto MaterialDTO
                    System.out.println("Material creado exitosamente con idMaterial: " + idGenerado);
                    return material; // Devolvemos el objeto actualizado
                } else {
                    System.err.println("No se pudo obtener el idMaterial generado.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar material en la base de datos: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene un material por su ID.
     * @param idMaterial El ID del material.
     * @return El material encontrado, o null si no se encontró.
     */
    public MaterialDTO obtenerMaterialPorID(int idMaterial) {
        MaterialDTO material = null;
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.select.obtenerMaterialPorID");

        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idMaterial);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MaterialDTO.TipoMaterial tipo = MaterialDTO.TipoMaterial.valueOf(rs.getString("tipo"));
                    boolean usoExterior = rs.getBoolean("usoExterior");
                    MaterialDTO.EstadoMaterial estado = MaterialDTO.EstadoMaterial.valueOf(rs.getString("estado"));
                    material = new MaterialDTO(idMaterial, tipo, usoExterior, estado);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener material por ID: " + e.getMessage());
        }
        return material;
    }

    /**
     * Obtiene todos los materiales disponibles en la base de datos.
     * @return Una lista de materiales disponibles.
     */
    public List<MaterialDTO> obtenerMaterialesDisponibles() {
        List<MaterialDTO> materiales = new ArrayList<>();
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.select.obtenerMaterialesDisponibles");

        try (Connection conn = dbConnection.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int idMaterial = rs.getInt("idMaterial");
                MaterialDTO.TipoMaterial tipo = MaterialDTO.TipoMaterial.valueOf(rs.getString("tipo"));
                boolean usoExterior = rs.getBoolean("usoExterior");
                MaterialDTO.EstadoMaterial estado = MaterialDTO.EstadoMaterial.valueOf(rs.getString("estado"));
                materiales.add(new MaterialDTO(idMaterial, tipo, usoExterior, estado));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener materiales disponibles: " + e.getMessage());
        }
        return materiales;
    }

    /**
     * Modifica el estado de un material en la base de datos.
     * @param idMaterial El ID del material a actualizar.
     * @param estado El nuevo estado del material.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarEstadoMaterial(int idMaterial, MaterialDTO.EstadoMaterial estado) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.update.actualizarEstadoMaterial");

        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, estado.name());
            stmt.setInt(2, idMaterial);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el estado del material: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los materiales registrados en la base de datos.
     * @return Una lista de todos los materiales.
     */
    public List<MaterialDTO> obtenerTodosLosMateriales() {
        List<MaterialDTO> materiales = new ArrayList<>();
        Properties sqlProperties = DBConnection.getSQLProperties();
        String query = sqlProperties.getProperty("sql.select.obtenerTodosLosMateriales");

        try (Connection conn = dbConnection.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int idMaterial = rs.getInt("idMaterial");
                MaterialDTO.TipoMaterial tipo = MaterialDTO.TipoMaterial.valueOf(rs.getString("tipo"));
                boolean usoExterior = rs.getBoolean("usoExterior");
                MaterialDTO.EstadoMaterial estado = MaterialDTO.EstadoMaterial.valueOf(rs.getString("estado"));

                MaterialDTO material = new MaterialDTO(idMaterial, tipo, usoExterior, estado);
                materiales.add(material);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los materiales: " + e.getMessage());
        }
        return materiales;
    }
}
