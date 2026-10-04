package ProyectoPW.src.servlets.client;

import ProyectoPW.src.dao.PistaDAO;
import ProyectoPW.src.dto.PistaDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Servlet encargado de gestionar la búsqueda de pistas por parte del usuario.
 * Este servlet permite a los usuarios buscar pistas disponibles según si son exteriores o no
 * y según la fecha seleccionada.
 */
@WebServlet("/BuscarPistaServlet")
public class BuscarPistaServlet extends HttpServlet {

    /**
     * Método que maneja las solicitudes HTTP GET para buscar pistas disponibles.
     * Permite a los usuarios buscar pistas según si son exteriores o no y una fecha seleccionada.
     * Este método valida los parámetros de entrada, realiza la búsqueda de las pistas
     * y pasa los resultados a la vista correspondiente.
     *
     * @param request La solicitud HTTP que contiene los parámetros necesarios para realizar la búsqueda.
     * @param response La respuesta HTTP que será enviada al cliente con los resultados.
     * @throws ServletException Si ocurre un error relacionado con el servlet.
     * @throws IOException Si ocurre un error relacionado con la entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener y validar los parámetros 'esExterior' y 'fecha' desde la solicitud
            String esExteriorStr = request.getParameter("esExterior");
            String fechaStr = request.getParameter("fecha");

            // Verificar si ambos parámetros son proporcionados
            if (esExteriorStr == null || fechaStr == null) {
                throw new IllegalArgumentException("Parámetros 'esExterior' y 'fecha' son obligatorios.");
            }

            // Convertir el parámetro 'esExterior' a un valor booleano
            boolean esExterior = Boolean.parseBoolean(esExteriorStr);
            // Convertir el parámetro 'fecha' a un objeto Date usando un formato específico
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);

            // Crear instancia de PistaDAO para realizar la búsqueda de las pistas disponibles
            PistaDAO pistaDAO = new PistaDAO();
            List<PistaDTO> pistasDisponibles = pistaDAO.buscarPistasPorTipoYFecha(esExterior, fecha);

            // Pasar la lista de pistas disponibles a la vista JSP
            request.setAttribute("pistas", pistasDisponibles);
            request.getRequestDispatcher("/mvc/view/client/buscarPistaView.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            // Manejo de errores en la validación de los parámetros
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/mvc/view/client/buscarPistaView.jsp").forward(request, response);
        } catch (Exception e) {
            // Manejo de otros errores que puedan ocurrir durante la ejecución
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al procesar la solicitud.");
        }
    }
}
