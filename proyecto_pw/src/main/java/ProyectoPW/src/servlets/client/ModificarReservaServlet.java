package ProyectoPW.src.servlets.client;

import ProyectoPW.src.dao.ReservaDAO;
import ProyectoPW.src.dao.PistaDAO;
import ProyectoPW.src.dto.ReservaDTO;
import ProyectoPW.src.dto.PistaDTO;
import ProyectoPW.src.display.UsuarioBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Servlet que permite al usuario modificar sus reservas.
 * Este servlet gestiona tanto la consulta de las reservas modificables
 * como la actualización de una reserva seleccionada por el usuario.
 */
@WebServlet("/ModificarReservaServlet")
public class ModificarReservaServlet extends HttpServlet {

    /**
     * Maneja la solicitud HTTP GET para cargar las reservas modificables del usuario y las pistas disponibles.
     * Se obtiene la sesión del usuario, y si está autenticado, se consulta la base de datos para obtener las reservas
     * y las pistas disponibles, enviándolas a la vista correspondiente.
     *
     * @param request La solicitud HTTP que contiene los parámetros de la solicitud.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error en la entrada/salida de datos.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener el UsuarioBean desde la sesión
            UsuarioBean usuarioBean = (UsuarioBean) request.getSession().getAttribute("usuarioBean");

            // Verificar si el usuario está autenticado
            if (usuarioBean == null || usuarioBean.getCorreo().isEmpty()) {
                request.setAttribute("error", "No se encontró el correo del usuario en la sesión o el usuario no está autenticado.");
                request.getRequestDispatcher("/mvc/view/modificarReservaView.jsp").forward(request, response);
                return;
            }

            String correoJugador = usuarioBean.getCorreo();

            // Consultar las reservas modificables del jugador
            ReservaDAO reservaDAO = new ReservaDAO();
            List<ReservaDTO> reservasModificables = reservaDAO.listarReservasModificables(correoJugador);

            // Consultar las pistas disponibles
            PistaDAO pistaDAO = new PistaDAO();
            List<PistaDTO> pistasDisponibles = pistaDAO.buscarPistasDisponibles();

            // Enviar las listas de reservas y pistas a la vista
            request.setAttribute("reservasModificables", reservasModificables);
            request.setAttribute("pistasDisponibles", pistasDisponibles);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno al procesar la solicitud.");
        }

        // Redirigir a la vista de modificación de reservas
        request.getRequestDispatcher("/mvc/view/client/modificarReservaView.jsp").forward(request, response);
    }

    /**
     * Maneja la solicitud HTTP POST para modificar una reserva existente.
     * Valida los datos de entrada y actualiza la reserva del usuario en la base de datos.
     * Si la modificación es exitosa, muestra un mensaje de éxito. Si ocurre un error, muestra un mensaje de error.
     *
     * @param request La solicitud HTTP que contiene los parámetros de la reserva a modificar.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error en la entrada/salida de datos.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener el UsuarioBean desde la sesión
            UsuarioBean usuarioBean = (UsuarioBean) request.getSession().getAttribute("usuarioBean");

            // Verificar si el usuario está autenticado
            if (usuarioBean == null || usuarioBean.getCorreo().isEmpty()) {
                throw new IllegalArgumentException("No se encontró el correo del usuario en la sesión o el usuario no está autenticado.");
            }

            String correoJugador = usuarioBean.getCorreo(); // Obtener el correo del usuario

            // Obtener y validar los parámetros de entrada
            String nuevaFechaYHoraStr = request.getParameter("nuevaFechaYHora");
            String nuevaDuracionStr = request.getParameter("nuevaDuracion");
            String nuevaPista = request.getParameter("nuevaPista");
            String numAdultosStr = request.getParameter("numAdultos");
            String numNinosStr = request.getParameter("numNinos");
            String reservaSeleccionada = request.getParameter("reservaSeleccionada");

            // Validar que todos los campos sean proporcionados
            if (nuevaFechaYHoraStr == null || nuevaDuracionStr == null || reservaSeleccionada == null || reservaSeleccionada.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }

            // Parsear los valores de los parámetros
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date nuevaFechaHora = sdf.parse(nuevaFechaYHoraStr);
            int nuevaDuracion = Integer.parseInt(nuevaDuracionStr);
            int numAdultos = (numAdultosStr != null && !numAdultosStr.isEmpty()) ? Integer.parseInt(numAdultosStr) : 0;
            int numNinos = (numNinosStr != null && !numNinosStr.isEmpty()) ? Integer.parseInt(numNinosStr) : 0;

            // Validar que los valores de adultos y niños no sean negativos
            if (numAdultos < 0) {
                throw new IllegalArgumentException("El número de adultos no puede ser negativo.");
            }
            if (numNinos < 0) {
                throw new IllegalArgumentException("El número de niños no puede ser negativo.");
            }

            // Verificar que la nueva fecha y hora no sean anteriores a la actual
            if (nuevaFechaHora.before(new Date())) {
                request.setAttribute("error", "La fecha y hora no pueden ser anteriores a la actual.");
                doGet(request, response); // Recargar los datos de la vista
                return;
            }

            // Buscar la reserva seleccionada por el usuario
            ReservaDAO reservaDAO = new ReservaDAO();
            List<ReservaDTO> reservasModificables = reservaDAO.listarReservasModificables(correoJugador);

            if (reservasModificables == null || reservasModificables.isEmpty()) {
                throw new IllegalArgumentException("No tienes reservas disponibles para modificar.");
            }

            ReservaDTO reservaExistente = null;
            // Buscar la reserva que coincida con la seleccionada por el usuario
            for (ReservaDTO reserva : reservasModificables) {
                if (reserva.getFechaHora().toString().equals(reservaSeleccionada)) {
                    reservaExistente = reserva;
                    break;
                }
            }

            // Verificar si la reserva seleccionada existe
            if (reservaExistente == null) {
                throw new IllegalArgumentException("No se encontró la reserva seleccionada para modificar.");
            }

            // Verificar restricciones sobre las reservas con bono
            boolean esReservaConBono = reservaExistente.getBono();
            if (esReservaConBono && !reservaExistente.getIdPista().equals(nuevaPista)) {
                throw new IllegalArgumentException("No se puede cambiar el tipo de pista para reservas asociadas a un bono.");
            }

            // Realizar la modificación de la reserva en la base de datos
            boolean reservaModificada = reservaDAO.modificarReserva(
                reservaExistente,
                nuevaFechaHora,
                nuevaDuracion,
                nuevaPista,
                numAdultos,
                numNinos
            );

            if (reservaModificada) {
                request.setAttribute("mensaje", "Reserva modificada correctamente.");
            } else {
                throw new SQLException("Error al modificar la reserva en la base de datos.");
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha inválido.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno al procesar la solicitud.");
        }

        // Llamar al doGet para recargar los datos de la vista
        doGet(request, response);
    }
}
