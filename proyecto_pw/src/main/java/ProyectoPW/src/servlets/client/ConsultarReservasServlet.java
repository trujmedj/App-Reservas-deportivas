package ProyectoPW.src.servlets.client;

import ProyectoPW.src.dao.ReservaDAO;
import ProyectoPW.src.dto.ReservaDTO;
import ProyectoPW.src.display.UsuarioBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servlet encargado de gestionar la consulta de reservas realizadas por el usuario.
 * Permite consultar las reservas entre dos fechas especificadas y las divide en futuras y finalizadas.
 */
@WebServlet("/ConsultarReservasServlet")
public class ConsultarReservasServlet extends HttpServlet {

    /**
     * Maneja las solicitudes HTTP GET para consultar las reservas realizadas por el usuario.
     * Valida las fechas de inicio y fin, y obtiene las reservas realizadas dentro de ese rango.
     * Luego, divide las reservas entre futuras y finalizadas y las pasa a la vista.
     *
     * @param request La solicitud HTTP que contiene los parámetros de fecha de inicio y fin.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error en el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error en el manejo de la entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros de fecha de inicio y fecha de fin
        String fechaInicioStr = request.getParameter("fechaInicio");
        String fechaFinStr = request.getParameter("fechaFin");

        // Obtener la sesión y verificar si el usuario está autenticado
        HttpSession session = request.getSession();
        UsuarioBean usuarioBean = (UsuarioBean) session.getAttribute("usuarioBean");

        // Si el usuario no está autenticado, redirigir a la vista de reserva con error
        if (usuarioBean == null || usuarioBean.getCorreo().isEmpty()) {
            request.setAttribute("error", "No se encontró un usuario autenticado en la sesión.");
            request.getRequestDispatcher("/mvc/view/client/realizarReservaBonoView.jsp").forward(request, response);
            return;
        }

        // Obtener el correo del usuario
        String correoUsuario = usuarioBean.getCorreo();

        // Validar que los parámetros de fechas no sean nulos
        if (fechaInicioStr == null || fechaFinStr == null || correoUsuario == null) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/mvc/view/client/consultarReservasView.jsp").forward(request, response);
            return;
        }

        // Crear un formato de fecha para parsear las fechas
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Convertir las fechas de los parámetros de String a Date
            Date fechaInicio = sdf.parse(fechaInicioStr);
            Date fechaFin = sdf.parse(fechaFinStr);

            // Validar que la fecha de inicio no sea posterior a la de fin
            if (fechaInicio.after(fechaFin)) {
                request.setAttribute("error", "La fecha de inicio no puede ser posterior a la fecha de fin.");
                request.getRequestDispatcher("/mvc/view/client/consultarReservasView.jsp").forward(request, response);
                return;
            }

            // Crear una instancia de ReservaDAO para obtener las reservas del usuario
            ReservaDAO reservaDAO = new ReservaDAO();
            List<ReservaDTO> todasReservas = reservaDAO.listarReservasEntreFechas(correoUsuario, fechaInicio, fechaFin);

            // Crear listas para almacenar las reservas futuras y finalizadas
            List<ReservaDTO> reservasFuturas = new ArrayList<>();
            List<ReservaDTO> reservasFinalizadas = new ArrayList<>();
            Date ahora = new Date(); // Obtener la fecha y hora actuales

            // Dividir las reservas entre futuras y finalizadas
            for (ReservaDTO reserva : todasReservas) {
                if (reserva.getFechaHora().after(ahora)) {
                    reservasFuturas.add(reserva); // Reserva futura
                } else {
                    reservasFinalizadas.add(reserva); // Reserva finalizada
                }
            }

            // Pasar las listas de reservas a la vista (JSP)
            request.setAttribute("reservasFuturas", reservasFuturas);
            request.setAttribute("reservasFinalizadas", reservasFinalizadas);
            request.setAttribute("success", "Reservas consultadas exitosamente.");

            // Redirigir a la página de consulta de reservas para mostrar los resultados
            request.getRequestDispatcher("/mvc/view/client/consultarReservasView.jsp").forward(request, response);

        } catch (ParseException e) {
            // Si ocurre un error en el formato de la fecha, mostrar un error
            e.printStackTrace();
            request.setAttribute("error", "Formato de fecha incorrecto.");
            request.getRequestDispatcher("/mvc/view/client/consultarReservasView.jsp").forward(request, response);
        }
    }
}
