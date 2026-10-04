    package ProyectoPW.src.dao;

import ProyectoPW.src.common.DBConnection;
import ProyectoPW.src.dto.ReservaDTO;
import ProyectoPW.src.dto.ReservaAdultosDTO;
import ProyectoPW.src.dto.ReservaInfantilDTO;
import ProyectoPW.src.dto.ReservaFamiliarDTO;
import ProyectoPW.src.dto.PistaDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class ReservaDAO extends DBConnection {

    public ReservaDAO() {
        // Constructor vacío
    }

    /**
     * Guarda una nueva reserva en la base de datos.
     */
    public boolean hacerReservaIndividual(ReservaDTO reserva) {
       Properties sqlProperties= DBConnection.getSQLProperties();
        String sql= sqlProperties.getProperty("sql.insert.hacerReservaIndividual");
        /*String sql = "INSERT INTO Reserva (emailJugador, fechaHoraReserva, duracionMinutos, nombrePista, precio, descuento, numAdultos, numNinos, bono) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";*/
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reserva.getCorreoUsuario());
            stmt.setTimestamp(2, new Timestamp(reserva.getFechaHora().getTime()));
            stmt.setInt(3, reserva.getDuracion());
            stmt.setString(4, reserva.getIdPista());
            stmt.setFloat(5, reserva.getPrecio());
            stmt.setFloat(6, reserva.getDescuento());
            stmt.setBoolean(9, reserva.getBono());

            // Configuramos los valores de numAdultos y numNinos dependiendo del tipo de reserva
            if (reserva instanceof ReservaAdultosDTO) {
                ReservaAdultosDTO reservaAdulto = (ReservaAdultosDTO) reserva;
                stmt.setInt(7, reservaAdulto.getNumAdultos());
                stmt.setInt(8, 0);  // No hay niños en una reserva de adultos
            } else if (reserva instanceof ReservaInfantilDTO) {
                ReservaInfantilDTO reservaInfantil = (ReservaInfantilDTO) reserva;
                stmt.setInt(7, 0);  // No hay adultos en una reserva infantil
                stmt.setInt(8, reservaInfantil.getNumNinos());
            } else if (reserva instanceof ReservaFamiliarDTO) {
                ReservaFamiliarDTO reservaFamiliar = (ReservaFamiliarDTO) reserva;
                stmt.setInt(7, reservaFamiliar.getNumAdultos());
                stmt.setInt(8, reservaFamiliar.getNumNinos());
            } else {
                // Si no es de ningún tipo conocido, lanza una excepción o usa valores por defecto
                throw new IllegalArgumentException("Tipo de reserva desconocido: " + reserva.getClass().getSimpleName());
            }

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al realizar la reserva: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
    }

    public boolean modificarReserva(ReservaDTO reservaAModificar, Date nuevaFechaHora, int nuevaDuracion, String nuevaPista, int nuevoNumAdultos, int nuevoNumNinos) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.update.modificarReserva");
        /*
        sql =
        UPDATE Reserva 
        SET fechaHoraReserva = ?, duracionMinutos = ?, nombrePista = ?, numAdultos = ?, numNinos = ? 
        WHERE emailJugador = ? AND fechaHoraReserva = ?;
        */
    
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Establecer los parámetros de la consulta
            stmt.setTimestamp(1, new Timestamp(nuevaFechaHora.getTime())); // Nueva fecha y hora
            stmt.setInt(2, nuevaDuracion); // Nueva duración
            stmt.setString(3, nuevaPista); // Nueva pista
            stmt.setInt(4, nuevoNumAdultos); // Nuevo número de adultos
            stmt.setInt(5, nuevoNumNinos); // Nuevo número de niños
            stmt.setString(6, reservaAModificar.getCorreoUsuario()); // Email del jugador
            stmt.setTimestamp(7, new Timestamp(reservaAModificar.getFechaHora().getTime())); // Fecha original de la reserva

        
            // Ejecutar la consulta
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Devuelve true si se actualizó al menos una fila
        } catch (SQLException e) {
            System.err.println("Error al modificar la reserva: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
    }
    
    

    /**
     * Modifica una reserva existente en la base de datos.
     */
    public boolean modificarReservaMini(ReservaDTO reserva, Date nuevaFechaHora, int nuevaDuracion) {
        Properties sqlProperties= DBConnection.getSQLProperties();
        String sql= sqlProperties.getProperty("sql.update.modificarReserva");
        //String sql = "UPDATE Reserva SET fechaHoraReserva = ?, duracionMinutos = ? WHERE emailJugador = ? AND fechaHoraReserva = ?";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(nuevaFechaHora.getTime()));
            stmt.setInt(2, nuevaDuracion);
            stmt.setString(3, reserva.getCorreoUsuario());
            stmt.setTimestamp(4, new Timestamp(reserva.getFechaHora().getTime()));
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar la reserva: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
    }

    /**
     * Cancela una reserva en la base de datos.
     */
    public boolean cancelarReserva(ReservaDTO reserva) {
        Properties sqlProperties= DBConnection.getSQLProperties();
        String sql= sqlProperties.getProperty("sql.delete.cancelarReserva");
        //String sql = "DELETE FROM Reserva WHERE emailJugador = ? AND fechaHoraReserva = ?";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reserva.getCorreoUsuario());
            stmt.setTimestamp(2, new Timestamp(reserva.getFechaHora().getTime()));
            
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error al cancelar la reserva: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
    }

    /**
     * Cuenta el número de reservas futuras a partir de la fecha actual.
     */
    public int contarReservasFuturas() {
        Properties sqlProperties= DBConnection.getSQLProperties();
        String sql= sqlProperties.getProperty("sql.select.contarReservasFuturas");
        //String sql = "SELECT COUNT(*) FROM Reserva WHERE fechaHoraReserva > ?";
        int count = 0;
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar las reservas futuras: " + e.getMessage());
        } finally {
            closeConnection();
        }
        
        return count;
    }

    /**
     * Lista las reservas por una fecha específica y pista.
     */
    public List<ReservaDTO> listarReservasPorFechaYPista(Date fecha, PistaDTO pista) {
        Properties sqlProperties= DBConnection.getSQLProperties();
        String sql= sqlProperties.getProperty("sql.select.listarReservasPorFechaYPista");
        //String sql = "SELECT * FROM Reserva WHERE DATE(fechaHoraReserva) = ? AND nombrePista = ?";
        List<ReservaDTO> reservas = new ArrayList<>();
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(fecha.getTime()));
            stmt.setString(2, pista.getNombre());
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String correoUsuario = rs.getString("emailJugador");
                Date fechaHora = rs.getTimestamp("fechaHoraReserva");
                int duracion = rs.getInt("duracionMinutos");
                String idPista = rs.getString("nombrePista");
                float precio = rs.getFloat("precio");
                float descuento = rs.getFloat("descuento");
                int numAdultos = rs.getInt("numAdultos");
                int numNinos = rs.getInt("numNinos");
                boolean bono = rs.getBoolean("bono");

                ReservaDTO reserva;
                if (numAdultos > 0 && numNinos == 0) {
                    // Reserva de tipo adulto
                    reserva = new ReservaAdultosDTO(correoUsuario, fechaHora, duracion, idPista, precio, descuento, numAdultos, bono);
                } else if (numAdultos == 0 && numNinos > 0) {
                    // Reserva de tipo infantil
                    reserva = new ReservaInfantilDTO(correoUsuario, fechaHora, duracion, idPista, precio, descuento, numNinos, bono);
                } else if (numAdultos > 0 && numNinos > 0) {
                    // Reserva de tipo familiar
                    reserva = new ReservaFamiliarDTO(correoUsuario, fechaHora, duracion, idPista, precio, descuento, numAdultos, numNinos, bono);
                } else {
                    // Ignorar si no cumple ninguna de las condiciones
                    continue;
                }

                reservas.add(reserva);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reservas por fecha y pista: " + e.getMessage());
        } finally {
            closeConnection();
        }
        
        return reservas;
    }
    
    public boolean eliminarReserva(ReservaDTO reserva) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.delete.eliminarReserva");
        // String sql = "DELETE FROM Reserva WHERE fechaHoraReserva = ? AND nombrePista = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new java.sql.Timestamp(reserva.getFechaHora().getTime()));
            stmt.setString(2, reserva.getIdPista());

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar la reserva: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
    }

    
    /**
     * Busca la reserva más reciente de un jugador dado su correo electrónico.
     *
     * @param correoJugador El correo electrónico del jugador.
     * @return Un objeto ReservaDTO si se encuentra una reserva; null si no existe ninguna.
     */
    public ReservaDTO encontrarReservaPorJugador(String correoJugador) {
        Properties sqlProperties= DBConnection.getSQLProperties();
        String sql= sqlProperties.getProperty("sql.select.encontrarReservaPorJugador");
        //String sql = "SELECT * FROM Reserva WHERE emailJugador = ? ORDER BY fechaHoraReserva DESC LIMIT 1";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correoJugador);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date fechaHora = rs.getTimestamp("fechaHoraReserva");
                int duracion = rs.getInt("duracionMinutos");
                String idPista = rs.getString("nombrePista");
                float precio = rs.getFloat("precio");
                float descuento = rs.getFloat("descuento");
                int numAdultos = rs.getInt("numAdultos");
                int numNinos = rs.getInt("numNinos");
                boolean bono = rs.getBoolean("bono");

                // Crear el objeto ReservaDTO según el tipo de reserva (Adultos, Infantil o Familiar)
                if (numAdultos > 0 && numNinos == 0) {
                    return new ReservaAdultosDTO(correoJugador, fechaHora, duracion, idPista, precio, descuento, numAdultos, bono);
                } else if (numAdultos == 0 && numNinos > 0) {
                    return new ReservaInfantilDTO(correoJugador, fechaHora, duracion, idPista, precio, descuento, numNinos, bono);
                } else if (numAdultos > 0 && numNinos > 0) {
                    return new ReservaFamiliarDTO(correoJugador, fechaHora, duracion, idPista, precio, descuento, numAdultos, numNinos, bono);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reserva por jugador: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return null; // Retorna null si no se encuentra ninguna reserva
    }


    /**
     * Obtiene la próxima reserva futura de un jugador dado su correo electrónico.
     *
     * @param correoJugador El correo electrónico del jugador.
     * @return Un objeto ReservaDTO que representa la próxima reserva futura del jugador; null si no hay reservas futuras.
     */
    public ReservaDTO obtenerProximaReserva(String correoJugador) {
        Properties sqlProperties= DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.select.obtenerProximaReserva");
        // String sql = "SELECT * FROM Reserva WHERE emailJugador = ? AND fechaHoraReserva > ? ORDER BY fechaHoraReserva ASC LIMIT 1";
    
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Establecemos el correo del jugador y la fecha actual
            stmt.setString(1, correoJugador);
            stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
        
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date fechaHora = rs.getTimestamp("fechaHoraReserva");
                int duracion = rs.getInt("duracionMinutos");
                String idPista = rs.getString("nombrePista");
                float precio = rs.getFloat("precio");
                float descuento = rs.getFloat("descuento");
                int numAdultos = rs.getInt("numAdultos");
                int numNinos = rs.getInt("numNinos");
                boolean bono = rs.getBoolean("bono");
            
                // Crear el objeto ReservaDTO según el tipo de reserva (Adultos, Infantil o Familiar)
                if (numAdultos > 0 && numNinos == 0) {
                    return new ReservaAdultosDTO(correoJugador, fechaHora, duracion, idPista, precio, descuento, numAdultos, bono);
                } else if (numAdultos == 0 && numNinos > 0) {
                    return new ReservaInfantilDTO(correoJugador, fechaHora, duracion, idPista, precio, descuento, numNinos, bono);
                } else if (numAdultos > 0 && numNinos > 0) {
                    return new ReservaFamiliarDTO(correoJugador, fechaHora, duracion, idPista, precio, descuento, numAdultos, numNinos, bono);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la próxima reserva: " + e.getMessage());
        } finally {
            closeConnection();
        }
    
        return null; // Retorna null si no hay reservas futuras
    }


    /**
     * Cuenta el número de reservas completadas por un jugador dado su correo electrónico.
     *
     * @param correoJugador El correo electrónico del jugador.
     * @return El número de reservas completadas por el jugador.
     */
    public int contarReservasCompletadas(String correoJugador) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.select.contarReservasCompletadas");
        // String sql = "SELECT COUNT(*) FROM Reserva WHERE emailJugador = ? AND fechaHoraReserva <= ?";

        int reservasCompletadas = 0;
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correoJugador);
            stmt.setTimestamp(2, new Timestamp(new Date().getTime())); // Fecha y hora actuales

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                reservasCompletadas = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar las reservas completadas: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return reservasCompletadas;
    }


    public List<ReservaDTO> listarReservasEntreFechas(String correoJugador, Date fechaInicio, Date fechaFin) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.select.listarReservasEntreFechas");
        // String sql = "SELECT * FROM Reserva WHERE emailJugador = ? AND fechaHoraReserva BETWEEN ? AND ?";
        List<ReservaDTO> reservas = new ArrayList<>();
    
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correoJugador);
            stmt.setTimestamp(2, new Timestamp(fechaInicio.getTime()));
            stmt.setTimestamp(3, new Timestamp(fechaFin.getTime()));
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String correoUsuario = rs.getString("emailJugador");
                Date fechaHora = rs.getTimestamp("fechaHoraReserva");
                int duracion = rs.getInt("duracionMinutos");
                String idPista = rs.getString("nombrePista");
                float precio = rs.getFloat("precio");
                float descuento = rs.getFloat("descuento");
                int numAdultos = rs.getInt("numAdultos");
                int numNinos = rs.getInt("numNinos");
                boolean bono = rs.getBoolean("bono");
                
                ReservaDTO reserva;
                if (numAdultos > 0 && numNinos == 0) {
                    // Reserva de tipo adulto
                    reserva = new ReservaAdultosDTO(correoUsuario, fechaHora, duracion, idPista, precio, descuento, numAdultos, bono);
                } else if (numAdultos == 0 && numNinos > 0) {
                    // Reserva de tipo infantil
                    reserva = new ReservaInfantilDTO(correoUsuario, fechaHora, duracion, idPista, precio, descuento, numNinos, bono);
                } else if (numAdultos > 0 && numNinos > 0) {
                    // Reserva de tipo familiar
                    reserva = new ReservaFamiliarDTO(correoUsuario, fechaHora, duracion, idPista, precio, descuento, numAdultos, numNinos, bono);
                } else {
                    // Ignorar si no cumple ninguna de las condiciones
                    continue;
                }
                
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reservas entre fechas: " + e.getMessage());
        } finally {
            closeConnection();
        }
    
        return reservas;
    }

    /**
     * Elimina todas las reservas futuras (reservas con fecha y hora posteriores a la actual).
     *
     * @return true si se eliminan reservas; false si no hay reservas futuras que eliminar o ocurre un error.
     */
    public boolean eliminarReservasFuturas() {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.delete.eliminarReservasFuturas");
        // String sql = "DELETE FROM Reserva WHERE fechaHoraReserva > ?";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(new Date().getTime())); // Fecha y hora actuales
            
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar reservas futuras: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
    }

    
    public List<ReservaDTO> listarReservasModificables(String correoJugador) {
        Properties sqlProperties = DBConnection.getSQLProperties();
        String sql = sqlProperties.getProperty("sql.select.listarReservasModificables");
        // String sql = "SELECT * FROM Reserva WHERE emailJugador = ? AND fechaHoraReserva > ?";
    
        List<ReservaDTO> reservas = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Establecer los parámetros de la consulta: correo del jugador y 24 horas después de la hora actual
            stmt.setString(1, correoJugador);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 24 horas desde ahora
        
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String emailJugador = rs.getString("emailJugador");
                Date fechaHora = rs.getTimestamp("fechaHoraReserva");
                int duracion = rs.getInt("duracionMinutos");
                String idPista = rs.getString("nombrePista");
                float precio = rs.getFloat("precio");
                float descuento = rs.getFloat("descuento");
                int numAdultos = rs.getInt("numAdultos");
                int numNinos = rs.getInt("numNinos");
                boolean bono = rs.getBoolean("bono");
            
                ReservaDTO reserva;
                if (numAdultos > 0 && numNinos == 0) {
                    // Reserva de tipo adulto
                    reserva = new ReservaAdultosDTO(emailJugador, fechaHora, duracion, idPista, precio, descuento, numAdultos, bono);
                } else if (numAdultos == 0 && numNinos > 0) {
                    // Reserva de tipo infantil
                    reserva = new ReservaInfantilDTO(emailJugador, fechaHora, duracion, idPista, precio, descuento, numNinos, bono);
                } else if (numAdultos > 0 && numNinos > 0) {
                    // Reserva de tipo familiar
                    reserva = new ReservaFamiliarDTO(emailJugador, fechaHora, duracion, idPista, precio, descuento, numAdultos, numNinos, bono);
                } else {
                    // Ignorar si no cumple ninguna de las condiciones
                    continue;
                }
            

                reservas.add(reserva);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reservas futuras: " + e.getMessage());
        } finally {
            closeConnection();
        }
    
        return reservas;
    }


    
}





