package ProyectoPW.src.servlets.admin;

import ProyectoPW.src.dao.ReservaDAO;
import ProyectoPW.src.dto.ReservaDTO;
import ProyectoPW.src.dto.PistaDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat; // Importar para formatear la fecha
import java.util.Date;
import java.util.List;

/**
 * Servlet que permite eliminar reservas futuras de pistas. 
 * Se pueden eliminar reservas específicas o todas las reservas futuras de forma masiva.
 */
@WebServlet("/EliminarReservasFuturasServlet")
public class EliminarReservasFuturasServlet extends HttpServlet {

    /**
     * Método que maneja las solicitudes POST para eliminar reservas futuras.
     * Dependiendo de la acción solicitada, elimina una reserva específica o todas las reservas futuras.
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a devolver.
     * @throws ServletException Si ocurre un error durante la ejecución del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Crear instancia de ReservaDAO para interactuar con la base de datos de reservas
        ReservaDAO reservaDAO = new ReservaDAO();

        try {
            // Obtener los parámetros del formulario enviados en la solicitud
            String accion = request.getParameter("accion"); // "especifica" o "todas"

            if ("especifica".equals(accion)) {
                // Eliminar una reserva específica

                // Obtener los datos de la reserva desde el formulario
                String nombrePista = request.getParameter("nombrePista");
                String fechaReservaStr = request.getParameter("fechaReserva");

                // Definir el formato de la fecha con hora
                SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

                // Convertir el string de fecha a un objeto Date
                Date fechaReserva = null;
                try {
                    fechaReserva = formatoFechaHora.parse(fechaReservaStr);
                } catch (Exception e) {
                    // Si el formato de fecha es incorrecto, se muestra un mensaje de error
                    request.setAttribute("error", "Formato de fecha incorrecto.");
                    request.getRequestDispatcher("/mvc/view/admin/eliminarReservasFuturasView.jsp").forward(request, response);
                    return;
                }

                // Crear un objeto PistaDTO con el nombre de la pista
                PistaDTO pista = new PistaDTO();
                pista.setNombre(nombrePista);

                // Obtener las reservas correspondientes a la fecha y pista especificadas
                List<ReservaDTO> reservas = reservaDAO.listarReservasPorFechaYPista(fechaReserva, pista);

                if (reservas.isEmpty()) {
                    // Si no se encontraron reservas para la pista y fecha, se muestra un mensaje de error
                    request.setAttribute("error", "No se encontraron reservas para la pista y fecha especificadas.");
                } else {
                    // Eliminar todas las reservas encontradas
                    for (ReservaDTO reserva : reservas) {
                        reservaDAO.eliminarReserva(reserva);
                    }
                    // Si las reservas se eliminaron con éxito, se muestra un mensaje de éxito
                    request.setAttribute("success", "Reserva(s) específica(s) eliminada(s) correctamente.");
                }

            } else if ("todas".equals(accion)) {
                // Eliminar todas las reservas futuras

                boolean reservasEliminadas = reservaDAO.eliminarReservasFuturas();

                if (reservasEliminadas) {
                    // Si las reservas futuras se eliminaron con éxito, se muestra un mensaje de éxito
                    request.setAttribute("success", "Todas las reservas futuras fueron eliminadas correctamente.");
                } else {
                    // Si no hay reservas futuras para eliminar, se muestra un mensaje de error
                    request.setAttribute("error", "No hay reservas futuras para eliminar.");
                }
            } else {
                // Si la acción no es válida, se muestra un mensaje de error
                request.setAttribute("error", "Acción no válida.");
            }
        } catch (Exception e) {
            // Si ocurre un error durante el proceso, se maneja la excepción y se muestra un mensaje de error
            request.setAttribute("error", "Ocurrió un error al procesar la solicitud: " + e.getMessage());
        }

        // Redirige de vuelta a la vista de eliminar reservas futuras
        request.getRequestDispatcher("/mvc/view/admin/eliminarReservasFuturasView.jsp").forward(request, response);
    }
}
