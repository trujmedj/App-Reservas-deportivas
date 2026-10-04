package ProyectoPW.src.servlets.admin;

import ProyectoPW.src.dao.PistaDAO;
import ProyectoPW.src.dao.MaterialDAO;
import ProyectoPW.src.dto.PistaDTO;
import ProyectoPW.src.dto.MaterialDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet que maneja la asociación de materiales a pistas en el sistema.
 * Permite tanto visualizar las pistas y materiales disponibles como asociar un material a una pista específica.
 */
@WebServlet("/AsociarMaterialServlet")
public class AsociarMaterialServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Método que maneja las solicitudes GET para mostrar las pistas y materiales disponibles en el sistema.
     * Carga las listas de pistas y materiales y las envía al JSP para que el administrador las pueda visualizar.
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a devolver.
     * @throws ServletException Si ocurre un error durante la ejecución del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PistaDAO pistaDAO = new PistaDAO(); // DAO para gestionar las pistas
        MaterialDAO materialDAO = new MaterialDAO(); // DAO para gestionar los materiales

        // Obtiene las listas de pistas y materiales disponibles
        List<PistaDTO> pistas = pistaDAO.obtenerTodasLasPistas();
        List<MaterialDTO> materiales = materialDAO.obtenerMaterialesDisponibles();

        // Verifica si las listas contienen datos
        if (pistas == null || pistas.isEmpty()) {
            System.out.println("No se han encontrado pistas.");
        }
        if (materiales == null || materiales.isEmpty()) {
            System.out.println("No se han encontrado materiales.");
        }

        // Establece los atributos en la solicitud para pasarlos al JSP
        request.setAttribute("pistas", pistas);
        request.setAttribute("materiales", materiales);

        // Redirige a la vista de asociar material a pista
        request.getRequestDispatcher("/mvc/view/admin/asociarMaterialPistaView.jsp").forward(request, response);
    }

    /**
     * Método que maneja las solicitudes POST para asociar un material a una pista.
     * Obtiene el nombre de la pista y el id del material, y realiza la asociación en la base de datos.
     * Luego, redirige a la vista de asociar material a pista.
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a devolver.
     * @throws ServletException Si ocurre un error durante la ejecución del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene los parámetros de la solicitud
        String nombrePista = request.getParameter("nombrePista");
        int idMaterial = Integer.parseInt(request.getParameter("idMaterial"));

        PistaDAO pistaDAO = new PistaDAO(); // DAO para gestionar las pistas

        // Intenta asociar el material a la pista
        boolean asociacionExitosa = pistaDAO.asociarMaterialAPista(nombrePista, idMaterial);

        // Establece un mensaje de éxito o error en función del resultado
        if (asociacionExitosa) {
            request.setAttribute("success", "Material asociado a la pista con éxito.");
        } else {
            request.setAttribute("error", "Error al asociar el material a la pista.");
        }

        // Vuelve a cargar las listas de pistas y materiales
        doGet(request, response);
    }
}
