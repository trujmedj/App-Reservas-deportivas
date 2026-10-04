package ProyectoPW.src.servlets.client;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import ProyectoPW.src.display.UsuarioBean;
import ProyectoPW.src.dto.BonoDTO;
import ProyectoPW.src.dto.JugadorDTO;
import ProyectoPW.src.dto.PistaDTO;
import ProyectoPW.src.dao.PistaDAO;
import ProyectoPW.src.dto.ReservaDTO;
import ProyectoPW.src.manager.GestorReservas;
import ProyectoPW.src.manager.GestorPistas;
import ProyectoPW.src.manager.GestorUsuarios;

import java.util.List;

/**
 * Servlet que permite a los usuarios realizar una reserva utilizando un bono.
 * Este servlet maneja tanto la visualización de los bonos disponibles y pistas
 * como el proceso de creación de una nueva reserva con un bono seleccionado.
 */
@WebServlet("/RealizarReservaBonoServlet")
public class RealizarReservaBonoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private GestorReservas gestorReservas = new GestorReservas();
    private GestorPistas gestorPistas = new GestorPistas();

    /**
     * Maneja la solicitud HTTP GET para cargar los bonos disponibles y las pistas.
     * Obtiene los bonos asociados al jugador y las pistas disponibles para realizar la reserva.
     * Si el usuario no está autenticado, redirige al formulario de reserva con un mensaje de error.
     * 
     * @param request La solicitud HTTP que contiene los parámetros de la solicitud.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error en la entrada/salida de datos.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UsuarioBean usuarioBean = (UsuarioBean) session.getAttribute("usuarioBean");

        // Verificar si el usuario está autenticado
        if (usuarioBean == null || usuarioBean.getCorreo().isEmpty()) {
            request.setAttribute("error", "No se encontró un usuario autenticado en la sesión.");
            request.getRequestDispatcher("/mvc/view/client/realizarReservaBonoView.jsp").forward(request, response);
            return;
        }

        String correoJugador = usuarioBean.getCorreo();
        try {
            // Obtener los bonos asociados al jugador
            List<BonoDTO> bonos = gestorReservas.obtenerBonosPorJugador(correoJugador);
            request.setAttribute("bonos", bonos);

            // Obtener las pistas disponibles
            PistaDAO pistaDAO = new PistaDAO();
            List<PistaDTO> pistas = pistaDAO.buscarPistasDisponibles();
            request.setAttribute("pistas", pistas);

            // Redirigir a la vista JSP
            request.getRequestDispatcher("/mvc/view/client/realizarReservaBonoView.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Ocurrió un error al cargar los datos: " + e.getMessage());
            request.getRequestDispatcher("/mvc/view/client/realizarReservaBonoView.jsp").forward(request, response);
        }
    }

    /**
     * Maneja la solicitud HTTP POST para procesar la reserva con un bono.
     * Valida los datos proporcionados por el usuario, verifica que la fecha
     * sea válida y posterior a la fecha actual, y realiza la reserva con el bono seleccionado.
     * Si la reserva es exitosa, muestra un mensaje de éxito, en caso contrario, muestra un error.
     *
     * @param request La solicitud HTTP que contiene los parámetros de la reserva.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error en la entrada/salida de datos.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UsuarioBean usuarioBean = (UsuarioBean) session.getAttribute("usuarioBean");

        // Verificar si el usuario está autenticado
        if (usuarioBean == null || usuarioBean.getCorreo().isEmpty()) {
            request.setAttribute("error", "No se encontró un usuario autenticado en la sesión.");
            request.getRequestDispatcher("/mvc/view/client/realizarReservaBonoView.jsp").forward(request, response);
            return;
        }

        String correoJugador = usuarioBean.getCorreo();
        JugadorDTO jugador = new GestorUsuarios().buscarUsuarioPorCorreo(correoJugador);

        // Verificar si el jugador existe
        if (jugador == null) {
            request.setAttribute("error", "Jugador no encontrado.");
            request.getRequestDispatcher("/mvc/view/client/realizarReservaBonoView.jsp").forward(request, response);
            return;
        }

        try {
            // Obtener el bono seleccionado
            int tipoBono = Integer.parseInt(request.getParameter("bonoSeleccionado"));
            BonoDTO bono = gestorReservas.buscarBonoPorJugadorYTipo(correoJugador, tipoBono);

            // Verificar si el bono existe
            if (bono == null) {
                request.setAttribute("error", "No se encontró un bono activo para este jugador.");
            } else {
                // Obtener la fecha y hora de la reserva
                String fechaHoraStr = request.getParameter("fechaHora");
                Date fechaHora = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(fechaHoraStr);

                // Verificar que la fecha sea posterior a la actual
                if (fechaHora.before(new Date())) {
                    request.setAttribute("error", "La fecha de la reserva debe ser posterior a la fecha actual.");
                } else {
                    // Obtener la duración y la pista seleccionada
                    int duracion = Integer.parseInt(request.getParameter("duracion"));
                    String nombrePista = request.getParameter("pista");
                    PistaDTO pista = gestorPistas.buscarPistaPorNombre(nombrePista);

                    // Verificar si la pista existe
                    if (pista == null) {
                        request.setAttribute("error", "La pista especificada no existe.");
                    } else {
                        // Obtener el número de adultos y niños
                        int numAdultos = Integer.parseInt(request.getParameter("numAdultos"));
                        int numNinos = Integer.parseInt(request.getParameter("numNinos"));

                        // Realizar la reserva
                        ReservaDTO reserva = gestorReservas.hacerReservaBono(jugador, fechaHora, duracion, pista, bono, numAdultos, numNinos);
                        if (reserva != null) {
                            request.getSession().setAttribute("success", "Reserva realizada con éxito.");
                        } else {
                            request.getSession().setAttribute("error", "No se pudo realizar la reserva con bono.");
                        }
                    }
                }
            }
        } catch (ParseException e) {
            request.getSession().setAttribute("error", "El formato de la fecha ingresada es inválido.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Por favor, ingrese valores numéricos válidos.");
        }

        // Redirigir a doGet para recargar los datos
        response.sendRedirect(request.getContextPath() + "/RealizarReservaBonoServlet");
    }
}
