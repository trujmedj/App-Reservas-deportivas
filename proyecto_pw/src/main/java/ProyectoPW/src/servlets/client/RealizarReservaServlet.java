package ProyectoPW.src.servlets.client;

import ProyectoPW.src.dao.PistaDAO;
import ProyectoPW.src.dto.PistaDTO;
import ProyectoPW.src.dto.ReservaDTO;
import ProyectoPW.src.dto.JugadorDTO;
import ProyectoPW.src.manager.GestorReservas;
import ProyectoPW.src.manager.GestorUsuarios;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ProyectoPW.src.display.UsuarioBean;

/**
 * Servlet que permite a los usuarios realizar una reserva de pista.
 * Este servlet maneja tanto la visualización de las pistas disponibles
 * como el proceso de creación de una nueva reserva.
 */
@WebServlet("/RealizarReservaServlet")
public class RealizarReservaServlet extends HttpServlet {

    /**
     * Maneja la solicitud HTTP GET para cargar las pistas disponibles.
     * Obtiene la lista de pistas disponibles y las envía a la vista para que el usuario las vea y realice la reserva.
     * 
     * @param request La solicitud HTTP que contiene los parámetros de la solicitud.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error en la entrada/salida de datos.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener la lista de pistas disponibles
            PistaDAO pistaDAO = new PistaDAO();
            List<PistaDTO> pistasDisponibles = pistaDAO.buscarPistasDisponibles();
            request.setAttribute("pistasDisponibles", pistasDisponibles);

            // Redirigir a la vista de la reserva
            request.getRequestDispatcher("/mvc/view/client/realizarReservaView.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar las pistas.");
        }
    }

    /**
     * Maneja la solicitud HTTP POST para procesar la reserva de pista.
     * Valida que la fecha, duración, número de personas y pista seleccionada sean correctos, 
     * y luego realiza la reserva. En caso de éxito o error, se muestra un mensaje adecuado.
     * 
     * @param request La solicitud HTTP que contiene los parámetros de la reserva.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error en la entrada/salida de datos.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener el usuarioBean desde la sesión
            UsuarioBean usuarioBean = (UsuarioBean) request.getSession().getAttribute("usuarioBean");
        
            // Verificar si el usuarioBean está presente en la sesión
            if (usuarioBean == null) {
                request.setAttribute("error", "No estás logueado. Por favor, inicia sesión.");
                request.getRequestDispatcher("/mvc/view/acceso/loginView.jsp").forward(request, response);
                return;
            }
        
            // Obtener el correo desde el usuarioBean
            String correo = usuarioBean.getCorreo();
        
            // Buscar el jugador en la base de datos a partir del correo
            JugadorDTO jugador = new GestorUsuarios().buscarUsuarioPorCorreo(correo);
            if (jugador == null) {
                request.setAttribute("error", "Jugador no encontrado.");
                request.getRequestDispatcher("/mvc/view/client/realizarReservaView.jsp").forward(request, response);
                return;
            }
        
            // Parsear la fecha y hora de la reserva
            String fechaHoraStr = request.getParameter("fechaHora");
            Date fechaHora = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(fechaHoraStr);
            if (fechaHora.before(new Date())) {
                request.setAttribute("error", "La fecha y hora no pueden ser anteriores a la actual.");
                // Cargar de nuevo las pistas
                PistaDAO pistaDAO = new PistaDAO();
                List<PistaDTO> pistasDisponibles = pistaDAO.buscarPistasDisponibles();
                request.setAttribute("pistasDisponibles", pistasDisponibles);
                request.getRequestDispatcher("/mvc/view/client/realizarReservaView.jsp").forward(request, response);
                return;
            }
        
            // Obtener la duración de la reserva
            int duracion = Integer.parseInt(request.getParameter("duracion"));
        
            // Obtener la pista seleccionada
            String pistaId = request.getParameter("pista");
            PistaDAO pistaDAO = new PistaDAO();
            PistaDTO pista = pistaDAO.obtenerPistaConMateriales(pistaId);
            
            // Verificar si la pista seleccionada está disponible
            if (pista == null) {
                request.setAttribute("error", "La pista seleccionada no está disponible.");
                // Cargar de nuevo las pistas
                List<PistaDTO> pistasDisponibles = pistaDAO.buscarPistasDisponibles();
                request.setAttribute("pistasDisponibles", pistasDisponibles);
                request.getRequestDispatcher("/mvc/view/client/realizarReservaView.jsp").forward(request, response);
                return;
            }
        
            // Obtener el número de adultos y niños
            int numAdultos = Integer.parseInt(request.getParameter("numAdultos"));
            int numNinos = Integer.parseInt(request.getParameter("numNinos"));

            // Validar que los números de personas no sean negativos
            if (numAdultos < 0 || numNinos < 0) {
                request.setAttribute("error", "El número de adultos o niños no puede ser negativo.");
                // Cargar de nuevo las pistas
                List<PistaDTO> pistasDisponibles = pistaDAO.buscarPistasDisponibles();
                request.setAttribute("pistasDisponibles", pistasDisponibles);
                request.getRequestDispatcher("/mvc/view/client/realizarReservaView.jsp").forward(request, response);
                return;
            }
        
            // Realizar la reserva
            GestorReservas gestorReservas = new GestorReservas();
            ReservaDTO reservaRealizada = gestorReservas.hacerReservaIndividual(jugador, fechaHora, duracion, pista, numAdultos, numNinos);
        
            // Verificar si la reserva fue exitosa y mostrar mensaje correspondiente
            if (reservaRealizada != null) {
                request.setAttribute("success", "Reserva realizada con éxito.");
            } else {
                request.setAttribute("error", "Error al realizar la reserva.");
            }
        
            // Cargar de nuevo las pistas disponibles para la siguiente acción
            List<PistaDTO> pistasDisponibles = pistaDAO.buscarPistasDisponibles();
            request.setAttribute("pistasDisponibles", pistasDisponibles);
        
            // Redirigir a la vista de realizarReservaView.jsp
            request.getRequestDispatcher("/mvc/view/client/realizarReservaView.jsp").forward(request, response);
        
        } catch (ParseException e) {
            // En caso de error con el formato de la fecha
            request.setAttribute("error", "Formato de fecha inválido.");
            // Cargar de nuevo las pistas disponibles
            PistaDAO pistaDAO = new PistaDAO();
            List<PistaDTO> pistasDisponibles = pistaDAO.buscarPistasDisponibles();
            request.setAttribute("pistasDisponibles", pistasDisponibles);
            request.getRequestDispatcher("/mvc/view/client/realizarReservaView.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la reserva.");
        }
    }
}
