package ProyectoPW.src.dao;

import ProyectoPW.src.dto.BonoDTO;
import ProyectoPW.src.dto.ReservaDTO;
import ProyectoPW.src.dto.ReservaAdultosDTO;
import ProyectoPW.src.dto.ReservaInfantilDTO;
import ProyectoPW.src.dto.ReservaFamiliarDTO;
import ProyectoPW.src.common.DBConnection;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties; // Para leer el archivo de propiedades (este lo tengo que meter en todos los DAO)

/**
 * Esta clase maneja la interacción con la base de datos para las operaciones relacionadas con los bonos.
 * Permite crear bonos, buscar bonos por jugador, hacer reservas con bono y obtener información sobre los bonos.
 * Extiende la clase {@link DBConnection} para utilizar la conexión a la base de datos.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class BonoDAO extends DBConnection {

    /**
     * Crea un nuevo bono en la base de datos.
     * 
     * @param bono Un objeto {@link BonoDTO} que contiene la información del bono a crear.
     * @return El objeto {@link BonoDTO} actualizado con el id generado, o null si ocurrió un error.
     */
    public BonoDTO crearBono(BonoDTO bono) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.insert.crearBono");

        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Asignamos los valores correspondientes de BonoDTO a la consulta
            stmt.setString(1, bono.getCorreoJugador());
            stmt.setDate(2, new java.sql.Date(bono.getFechaCreacion().getTime()));
            stmt.setDate(3, new java.sql.Date(bono.getFechaCaducidad().getTime()));
            stmt.setInt(4, bono.getSesionesRestantes());
            stmt.setInt(5, bono.getTipoReserva());

            // Ejecutamos la inserción
            stmt.executeUpdate();

            // Obtenemos la clave generada automáticamente (idBono)
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    bono.setIdBono(idGenerado); // Actualizamos el id en el objeto BonoDTO
                    System.out.println("Bono creado exitosamente con idBono: " + idGenerado);
                    return bono; // Devolvemos el objeto actualizado
                } else {
                    System.err.println("No se pudo obtener el idBono generado.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear el bono: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            closeConnection();
        }
    }

    /**
     * Realiza una reserva con el bono especificado.
     * 
     * @param bono Un objeto {@link BonoDTO} que representa el bono que se usará.
     * @param reserva Un objeto {@link ReservaDTO} que contiene la información de la reserva.
     * @return {@code true} si la reserva se realizó correctamente, {@code false} en caso contrario.
     */
    public boolean hacerReservaBono(BonoDTO bono, ReservaDTO reserva) {
        if (bono.getSesionesRestantes() <= 0) {
            System.out.println("El bono no tiene sesiones restantes.");
            return false;
        }
    
        if (new Date().after(bono.getFechaCaducidad())) {
            System.out.println("El bono ha caducado.");
            return false;
        }

        float descuento = 0.95f;

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            Properties sqlProperties = DBConnection.getSQLProperties();
            String reservaSql = sqlProperties.getProperty("sql.insert.reservaBono");
            String updateBonoSql = sqlProperties.getProperty("sql.update.hacerReservaBono");
            String eliminarBonoSql = sqlProperties.getProperty("sql.delete.eliminarBono");

            try (PreparedStatement stmt = conn.prepareStatement(reservaSql)) {
                stmt.setString(1, bono.getCorreoJugador());
                stmt.setTimestamp(2, new Timestamp(reserva.getFechaHora().getTime()));
                stmt.setInt(3, reserva.getDuracion());
                stmt.setString(4, reserva.getIdPista());
                stmt.setFloat(5, reserva.getPrecio() * descuento);
                stmt.setFloat(6, descuento);

                // Asignación de valores específicos según el tipo de bono y reserva
                if (bono.getTipoReserva() == 1 && reserva instanceof ReservaInfantilDTO) {
                    stmt.setInt(7, 0);
                    stmt.setInt(8, ((ReservaInfantilDTO) reserva).getNumNinos());
                } else if (bono.getTipoReserva() == 2 && reserva instanceof ReservaFamiliarDTO) {
                    stmt.setInt(7, ((ReservaFamiliarDTO) reserva).getNumAdultos());
                    stmt.setInt(8, ((ReservaFamiliarDTO) reserva).getNumNinos());
                } else if (bono.getTipoReserva() == 3 && reserva instanceof ReservaAdultosDTO) {
                    stmt.setInt(7, ((ReservaAdultosDTO) reserva).getNumAdultos());
                    stmt.setInt(8, 0);
                } else {
                    stmt.setInt(7, 0);
                    stmt.setInt(8, 0);
                }

                stmt.setBoolean(9, reserva.getBono());
                stmt.executeUpdate();
            }

            bono.usarSesion();

            // Si se ha agotado el bono, eliminamos el bono de la base de datos
            if (bono.getSesionesRestantes() == 0) {
                System.out.println("El bono ha agotado sus sesiones.");
                try (PreparedStatement eliminarStmt = conn.prepareStatement(eliminarBonoSql)) {
                    eliminarStmt.setInt(1, bono.getIdBono());
                    eliminarStmt.executeUpdate();
                    System.out.println("El bono con ID " + bono.getIdBono() + " ha sido eliminado de la base de datos.");
                } catch (SQLException e) {
                    conn.rollback();
                    System.err.println("Error al eliminar el bono de la base de datos: " + e.getMessage());
                    return false;
                }
            } else {
                try (PreparedStatement bonoStmt = conn.prepareStatement(updateBonoSql)) {
                    bonoStmt.setInt(1, bono.getSesionesRestantes());
                    bonoStmt.setInt(2, bono.getIdBono());
                    bonoStmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
    }

    /**
     * Busca un bono asociado a un jugador específico mediante su correo electrónico.
     * 
     * @param correoJugador El correo electrónico del jugador.
     * @return Un objeto {@link BonoDTO} si se encuentra el bono; {@code null} en caso contrario.
     */
    public BonoDTO buscarBonoPorJugador(String correoJugador) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.select.buscarBonoPorJugador");
        BonoDTO bono = null;

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correoJugador);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idBono = rs.getInt("idBono");
                Date fechaCreacion = rs.getDate("fechaCreacion");
                int tipoReserva = rs.getInt("tipoBono");
                bono = new BonoDTO(idBono, correoJugador, fechaCreacion, tipoReserva);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el bono para el jugador: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return bono;
    }

    /**
     * Obtiene una lista de bonos asociados a un jugador específico mediante su correo electrónico.
     * 
     * @param correoJugador El correo electrónico del jugador.
     * @return Una lista de objetos {@link BonoDTO} si se encuentran bonos; lista vacía en caso contrario.
     */
    public List<BonoDTO> obtenerBonosPorJugador(String correoJugador) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.select.obtenerBonosPorJugador");

        List<BonoDTO> bonos = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correoJugador);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idBono = rs.getInt("idBono");
                Date fechaCreacion = rs.getDate("fechaCreacion");
                int tipoReserva = rs.getInt("tipoBono");
                int sesionesRestantes = rs.getInt("sesionesRestantes");
                Date fechaCaducidad = rs.getDate("fechaCaducidad");

                BonoDTO bono = new BonoDTO(idBono, correoJugador, fechaCreacion, tipoReserva);
                bono.setFechaCaducidad(fechaCaducidad);
                bono.setSesionesRestantes(sesionesRestantes);
                bonos.add(bono);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los bonos para el jugador: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return bonos;
    }

    /**
     * Busca un bono asociado a un jugador específico mediante su correo electrónico y tipo de bono.
     * 
     * @param correoJugador El correo electrónico del jugador.
     * @param tipoBono El tipo de bono (1: Infantil, 2: Familiar, 3: Adultos).
     * @return Un objeto {@link BonoDTO} si se encuentra un bono coincidente; {@code null} en caso contrario.
     */
    public BonoDTO buscarBonoPorJugadorYTipo(String correoJugador, int tipoBono) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.select.buscarBonoPorJugadorYTipo");
        BonoDTO bono = null;

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correoJugador);
            stmt.setInt(2, tipoBono);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idBono = rs.getInt("idBono");
                Date fechaCreacion = rs.getDate("fechaCreacion");
                int tipoReserva = rs.getInt("tipoBono");
                int sesionesRestantes = rs.getInt("sesionesRestantes");
                Date fechaCaducidad = rs.getDate("fechaCaducidad");

                bono = new BonoDTO(idBono, correoJugador, fechaCreacion, tipoReserva);
                bono.setSesionesRestantes(sesionesRestantes);
                bono.setFechaCaducidad(fechaCaducidad);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el bono para el jugador y tipo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return bono;
    }
}
