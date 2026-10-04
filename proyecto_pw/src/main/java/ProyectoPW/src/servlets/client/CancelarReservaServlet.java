package ProyectoPW.src.servlets.client;

import ProyectoPW.src.dao.ReservaDAO;
import ProyectoPW.src.dto.ReservaDTO;
import ProyectoPW.src.display.UsuarioBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Servlet encargado de gestionar la visualización y cancelación de reservas de los usuarios.
 * Permite a los usuarios ver sus reservas modificables y cancelar una reserva específica.
 */
@WebServlet("/CancelarReservaServlet")
public class CancelarReservaServlet extends HttpServlet {

    // Instancia de ReservaDAO para gestionar las operaciones con la base de datos
    private ReservaDAO reservaDAO;

    /**
     * Método de inicialización del servlet, donde se crea la instancia del DAO de reservas.
     */
    @Override
    public void init() throws ServletException {
        reservaDAO = new ReservaDAO(); // Inicializa el DAO de reservas
    }

    /**
     * Método que maneja las solicitudes HTTP GET.
     * Este método obtiene las reservas modificables del usuario autenticado y las muestra en la vista.
     * 
     * @param request La solicitud HTTP que contiene los parámetros necesarios.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error relacionado con el servlet.
     * @throws IOException Si ocurre un error relacionado con la entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            // Obtener el UsuarioBean desde la sesión del usuario
            UsuarioBean usuarioBean = (UsuarioBean) request.getSession().getAttribute("usuarioBean");

            // Verificar que el usuario está autenticado
            if (usuarioBean == null || usuarioBean.getCorreo().isEmpty()) {
                request.setAttribute("error", "No se encontró el correo del usuario en la sesión o el usuario no está autenticado.");
                request.getRequestDispatcher("/mvc/view/client/cancelarReservaView.jsp").forward(request, response);
                return;
            }

            // Obtener el correo del usuario
            String correoJugador = usuarioBean.getCorreo();

            // Listar las reservas modificables del usuario
            List<ReservaDTO> reservas = reservaDAO.listarReservasModificables(correoJugador);

            // Verificar si el usuario tiene reservas modificables
            if (reservas != null && !reservas.isEmpty()) {
                // Enviar las reservas a la vista para que el usuario las vea
                request.setAttribute("reservas", reservas);
            } else {
                // Si no tiene reservas modificables, mostrar un mensaje de error
                request.setAttribute("error", "No tienes reservas modificables.");
            }

        } catch (Exception e) {
            // En caso de error inesperado, mostrar mensaje de error
            request.setAttribute("error", "Ocurrió un error inesperado: " + e.getMessage());
        }

        // Redirigir a la página de gestión de reservas
        request.getRequestDispatcher("/mvc/view/client/cancelarReservaView.jsp").forward(request, response);
    }

    /**
     * Método que maneja las solicitudes HTTP POST.
     * Este método recibe la solicitud de cancelación de una reserva por parte del usuario.
     * Valida si la reserva existe y luego la cancela utilizando el DAO de reservas.
     * 
     * @param request La solicitud HTTP que contiene los parámetros para cancelar la reserva.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error relacionado con el servlet.
     * @throws IOException Si ocurre un error relacionado con la entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener el UsuarioBean desde la sesión
            UsuarioBean usuarioBean = (UsuarioBean) request.getSession().getAttribute("usuarioBean");

            // Verificar que el usuario está autenticado
            if (usuarioBean == null || usuarioBean.getCorreo().isEmpty()) {
                request.setAttribute("error", "No se encontró el correo del usuario en la sesión o el usuario no está autenticado.");
                request.getRequestDispatcher("/mvc/view/client/cancelarReservaView.jsp").forward(request, response);
                return;
            }

            // Obtener el correo del usuario
            String correoJugador = usuarioBean.getCorreo();

            // Obtener la fecha y hora de la reserva que el usuario desea cancelar
            String fechaHoraReservaStr = request.getParameter("fechaHoraReserva");
            if (fechaHoraReservaStr == null || fechaHoraReservaStr.isEmpty()) {
                request.setAttribute("error", "No se seleccionó ninguna reserva para cancelar.");
                request.getRequestDispatcher("/mvc/view/client/cancelarReservaView.jsp").forward(request, response);
                return;
            }

            // Convertir la fecha y hora de la reserva desde el parámetro
            Date fechaHoraReserva = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fechaHoraReservaStr);

            // Listar las reservas modificables
            List<ReservaDTO> reservas = reservaDAO.listarReservasModificables(correoJugador);
            ReservaDTO reservaSeleccionada = null;

            // Crear el formato de fecha para comparar
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

            // Buscar la reserva que coincide con el correo del jugador y la fecha
            for (ReservaDTO reserva : reservas) {
                // Convertir las fechas a cadenas para compararlas
                String fechaReservaStr = sdf.format(reserva.getFechaHora()); // reserva.getFechaHora() es Date
                String fechaCancelarStr = sdf.format(fechaHoraReserva); // fechaHoraReserva es Date
                
                // Limpiar el correo
                String correoLimpio = correoJugador.trim();

                // Comparar correo y fechas formateadas
                if (reserva.getCorreoUsuario().trim().equals(correoLimpio) &&
                    fechaReservaStr.equals(fechaCancelarStr)) {
                    reservaSeleccionada = reserva;
                    break;
                }
            }

            // Si no se encuentra la reserva, mostrar un error
            if (reservaSeleccionada == null) {
                request.setAttribute("error", "Reserva no encontrada.");
                request.getRequestDispatcher("/mvc/view/client/cancelarReservaView.jsp").forward(request, response);
                return;
            }

            // Cancelar la reserva utilizando el ReservaDAO
            boolean cancelada = reservaDAO.cancelarReserva(reservaSeleccionada);
            if (cancelada) {
                // Si la cancelación fue exitosa, mostrar un mensaje de éxito
                request.setAttribute("success", "Reserva cancelada exitosamente.");
            } else {
                // Si la cancelación falló, mostrar un mensaje de error
                request.setAttribute("error", "No se pudo cancelar la reserva. Intente nuevamente.");
            }

        } catch (Exception e) {
            // En caso de error inesperado, mostrar mensaje de error
            request.setAttribute("error", "Ocurrió un error inesperado: " + e.getMessage());
        }

        // Recargar las reservas disponibles llamando a doGet()
        doGet(request, response);

        // Redirigir a la página de gestión de reservas
        request.getRequestDispatcher("/mvc/view/client/cancelarReservaView.jsp").forward(request, response);
    }
}
