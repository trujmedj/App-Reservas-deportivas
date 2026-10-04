package ProyectoPW.src.servlets.admin;

import ProyectoPW.src.dao.MaterialDAO;
import ProyectoPW.src.dao.PistaDAO;
import ProyectoPW.src.dto.MaterialDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet encargado de modificar el estado de un material o una pista.
 * Dependiendo del tipo de objeto (material o pista), actualiza su estado correspondiente.
 */
@WebServlet("/ModificarEstadoServlet")
public class ModificarEstadoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Método que maneja las solicitudes POST para modificar el estado de un material o una pista.
     * Dependiendo del tipo de objeto, el estado puede ser modificado tanto para un material como para una pista.
     *
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a devolver.
     * @throws ServletException Si ocurre un error durante la ejecución del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener parámetros de la solicitud
        String tipoObjeto = request.getParameter("tipoObjeto"); // "material" o "pista"
        boolean operacionExitosa = false;

        try {
            // Si el objeto a modificar es un material
            if ("material".equalsIgnoreCase(tipoObjeto)) {
                // Obtener el ID del material y el nuevo estado del material
                int idMaterial = Integer.parseInt(request.getParameter("idMaterial"));
                String estadoMaterialStr = request.getParameter("estadoMaterial");

                // Convertir el estado recibido (String) en un valor del enum EstadoMaterial
                MaterialDTO.EstadoMaterial estadoMaterial = MaterialDTO.EstadoMaterial.valueOf(estadoMaterialStr.toUpperCase());

                // Crear instancia de MaterialDAO y actualizar el estado del material
                MaterialDAO materialDAO = new MaterialDAO();
                operacionExitosa = materialDAO.actualizarEstadoMaterial(idMaterial, estadoMaterial);

            // Si el objeto a modificar es una pista
            } else if ("pista".equalsIgnoreCase(tipoObjeto)) {
                // Obtener el nombre de la pista y el nuevo estado de la pista
                String nombrePista = request.getParameter("nombrePista");
                String estadoPistaStr = request.getParameter("estadoPista");

                // Convertir el estado recibido (DISPONIBLE/NO DISPONIBLE) a un valor booleano
                boolean estadoDisponible = "DISPONIBLE".equalsIgnoreCase(estadoPistaStr);

                // Crear instancia de PistaDAO y modificar el estado de la pista
                PistaDAO pistaDAO = new PistaDAO();
                operacionExitosa = pistaDAO.modificarEstadoPista(nombrePista, estadoDisponible);
            }

            // Configurar el mensaje de éxito o error dependiendo del resultado de la operación
            if (operacionExitosa) {
                request.setAttribute("success", "Estado modificado con éxito.");
            } else {
                request.setAttribute("error", "Error al modificar el estado.");
            }
        } catch (IllegalArgumentException e) {
            // Manejo específico para valores inválidos en los enums (por ejemplo, estado de material o pista incorrecto)
            e.printStackTrace();
            request.setAttribute("error", "El estado proporcionado no es válido.");
        } catch (Exception e) {
            // Manejo genérico de excepciones, en caso de error interno del servidor
            e.printStackTrace();
            request.setAttribute("error", "Se produjo un error interno al procesar la solicitud.");
        }

        // Redirigir al JSP correspondiente para mostrar el resultado
        request.getRequestDispatcher("/mvc/view/admin/modificarEstadoView.jsp").forward(request, response);
    }
}
