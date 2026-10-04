package ProyectoPW.src.servlets.admin;

import ProyectoPW.src.dao.MaterialDAO;
import ProyectoPW.src.dao.PistaDAO;
import ProyectoPW.src.dto.MaterialDTO;
import ProyectoPW.src.dto.PistaDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet que maneja las solicitudes de alta de nuevos materiales y pistas en el sistema.
 * Dependiendo del tipo recibido en la solicitud, se procederá a crear un material o una pista.
 */
@WebServlet("/admin/alta")
public class AltaServlet extends HttpServlet {

    private MaterialDAO materialDAO = new MaterialDAO(); // DAO para gestionar los materiales
    private PistaDAO pistaDAO = new PistaDAO(); // DAO para gestionar las pistas

    /**
     * Método que maneja las solicitudes POST para crear materiales o pistas.
     * Dependiendo del parámetro 'tipo', se redirige la solicitud al método correspondiente.
     * 
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a devolver.
     * @throws ServletException Si ocurre un error durante la ejecución del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tipo = request.getParameter("tipo"); // Puede ser "material" o "pista"

        if ("material".equalsIgnoreCase(tipo)) {
            // Si es tipo material, se llama al método para crear material
            crearMaterial(request, response);
        } else if ("pista".equalsIgnoreCase(tipo)) {
            // Si es tipo pista, se llama al método para crear pista
            crearPista(request, response);
        } else {
            // Si el tipo no es reconocido, se devuelve un error
            request.setAttribute("error", "Tipo no reconocido. Use 'material' o 'pista'.");
            request.getRequestDispatcher("/mvc/view/admin/altaMaterialView.jsp").forward(request, response);
        }
    }

    /**
     * Crea un nuevo material en el sistema y redirige a la vista correspondiente.
     * Si hay algún error, se muestra un mensaje de error.
     * 
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a devolver.
     * @throws ServletException Si ocurre un error durante la ejecución del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private void crearMaterial(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Recibe los parámetros necesarios para crear el material
            String tipoMaterialParam = request.getParameter("tipoMaterial");
            boolean usoExterior = Boolean.parseBoolean(request.getParameter("usoExterior"));
            String estadoParam = request.getParameter("estado");

            // Verifica que los parámetros obligatorios estén presentes
            if (tipoMaterialParam == null || estadoParam == null) {
                request.setAttribute("error", "Faltan parámetros obligatorios.");
                request.getRequestDispatcher("/mvc/view/admin/altaMaterialView.jsp").forward(request, response);
                return;
            }

            // Convierte los parámetros recibidos en valores enum correspondientes
            MaterialDTO.TipoMaterial tipoMaterial = MaterialDTO.TipoMaterial.valueOf(tipoMaterialParam.toUpperCase());
            MaterialDTO.EstadoMaterial estadoMaterial = MaterialDTO.EstadoMaterial.valueOf(estadoParam.toUpperCase());

            // Crea el objeto MaterialDTO con los parámetros recibidos
            MaterialDTO material = new MaterialDTO(0, tipoMaterial, usoExterior, estadoMaterial);
            // Inserta el nuevo material en la base de datos
            MaterialDTO materialCreado = materialDAO.insertarMaterial(material);

            if (materialCreado != null) {
                // Si el material fue creado correctamente, se muestra un mensaje de éxito
                request.setAttribute("success", "Material creado con éxito: " + materialCreado.getIdMaterial());
            } else {
                // Si hubo un error al crear el material, se muestra un mensaje de error
                request.setAttribute("error", "Error al crear el material.");
            }
            // Redirige a la vista de alta de material
            request.getRequestDispatcher("/mvc/view/admin/altaMaterialView.jsp").forward(request, response);
        } catch (Exception e) {
            // Si ocurre un error en el servidor, se muestra un mensaje de error
            request.setAttribute("error", "Error en el servidor: " + e.getMessage());
            request.getRequestDispatcher("/mvc/view/admin/altaMaterialView.jsp").forward(request, response);
        }
    }

    /**
     * Crea una nueva pista en el sistema y redirige a la vista correspondiente.
     * Si hay algún error, se muestra un mensaje de error.
     * 
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a devolver.
     * @throws ServletException Si ocurre un error durante la ejecución del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private void crearPista(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Recibe los parámetros necesarios para crear la pista
            String nombre = request.getParameter("nombre");
            boolean disponible = Boolean.parseBoolean(request.getParameter("disponible"));
            boolean esExterior = Boolean.parseBoolean(request.getParameter("esExterior"));
            String tamanoParam = request.getParameter("tamano");
            int maxJugadores = Integer.parseInt(request.getParameter("maxJugadores"));

            // Verifica que los parámetros obligatorios estén presentes
            if (nombre == null || tamanoParam == null) {
                request.setAttribute("error", "Faltan parámetros obligatorios.");
                request.getRequestDispatcher("/mvc/view/altaPistaView.jsp").forward(request, response);
                return;
            }

            // Verifica que el número máximo de jugadores no sea negativo
            if (maxJugadores < 0) {
                request.setAttribute("error", "El número máximo de jugadores no puede ser negativo.");
                request.getRequestDispatcher("/mvc/view/altaPistaView.jsp").forward(request, response);
                return;
            }

            // Convierte el tamaño de la pista recibido a un valor enum correspondiente
            PistaDTO.TamanoPista tamano = PistaDTO.TamanoPista.valueOf(tamanoParam.toUpperCase());

            // Crea el objeto PistaDTO con los parámetros recibidos
            PistaDTO pista = new PistaDTO(nombre, disponible, esExterior, tamano, maxJugadores);
            // Inserta la nueva pista en la base de datos
            boolean insertada = pistaDAO.insertarPista(pista);

            if (insertada) {
                // Si la pista fue creada correctamente, se muestra un mensaje de éxito
                request.setAttribute("success", "Pista creada con éxito: " + pista.getNombre());
            } else {
                // Si hubo un error al crear la pista, se muestra un mensaje de error
                request.setAttribute("error", "Error al crear la pista.");
            }
            // Redirige a la vista de alta de pista
            request.getRequestDispatcher("/mvc/view/altaPistaView.jsp").forward(request, response);
        } catch (Exception e) {
            // Si ocurre un error en el servidor, se muestra un mensaje de error
            request.setAttribute("error", "Error en el servidor: " + e.getMessage());
            request.getRequestDispatcher("/mvc/view/altaPistaView.jsp").forward(request, response);
        }
    }
}
