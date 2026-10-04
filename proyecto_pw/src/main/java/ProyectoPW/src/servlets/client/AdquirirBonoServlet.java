package ProyectoPW.src.servlets.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ProyectoPW.src.manager.GestorReservas;
import ProyectoPW.src.dao.BonoDAO;
import ProyectoPW.src.dto.BonoDTO;
import ProyectoPW.src.display.UsuarioBean;
import java.io.IOException;

/**
 * Servlet encargado de gestionar la creación de bonos para reservas por parte de los usuarios.
 * Este servlet permite a los usuarios adquirir bonos para realizar reservas en el sistema.
 */
@WebServlet("/AdquirirBonoServlet")
public class AdquirirBonoServlet extends HttpServlet {

    // Gestor encargado de manejar la lógica de las reservas y bonos
    private GestorReservas gestorReservas;

    /**
     * Inicializa el servlet. Este método se ejecuta cuando se inicializa el servlet
     * y se utiliza para preparar el entorno necesario, como la creación del gestor de reservas.
     *
     * @throws ServletException si ocurre un error durante la inicialización.
     */
    @Override
    public void init() throws ServletException {
        gestorReservas = new GestorReservas(); // Inicializa el gestor de reservas
    }

    /**
     * Método que maneja las solicitudes POST para la adquisición de un bono.
     * Permite a un usuario adquirir un bono para un tipo específico de reserva.
     * El proceso verifica que el usuario esté autenticado, que el tipo de reserva sea válido
     * y que el usuario no tenga un bono ya adquirido para ese tipo de reserva.
     *
     * @param request La solicitud HTTP que contiene los parámetros del formulario.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error relacionado con el servlet.
     * @throws IOException Si ocurre un error relacionado con la entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        try {
            // Obtener el UsuarioBean de la sesión del usuario autenticado
            UsuarioBean usuarioBean = (UsuarioBean) request.getSession().getAttribute("usuarioBean");

            // Verificar si el usuario está autenticado, es decir, si se encuentra en la sesión
            if (usuarioBean == null || usuarioBean.getCorreo().isEmpty()) {
                request.setAttribute("error", "No se encontró el correo del usuario en la sesión o el usuario no está autenticado.");
                request.getRequestDispatcher("/mvc/view/client/adquirirBonoView.jsp").forward(request, response);
                return;
            }

            // Obtener el correo del jugador desde el UsuarioBean
            String correoJugador = usuarioBean.getCorreo();

            // Obtener el tipo de reserva del formulario
            int tipoReserva;
            try {
                tipoReserva = Integer.parseInt(request.getParameter("tipoReserva"));
                // Validar que el tipo de reserva sea uno de los valores válidos (1, 2, o 3)
                if (tipoReserva < 1 || tipoReserva > 3) {
                    throw new IllegalArgumentException("Tipo de reserva inválido.");
                }
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Tipo de reserva inválido. Seleccione una opción válida.");
                request.getRequestDispatcher("/mvc/view/client/adquirirBonoView.jsp").forward(request, response);
                return;
            }

            // Instanciar BonoDAO para buscar si el jugador ya tiene un bono para el tipo de reserva
            BonoDAO bonoDAO = new BonoDAO();
            BonoDTO bonoExistente = bonoDAO.buscarBonoPorJugadorYTipo(correoJugador, tipoReserva);

            // Verificar si ya existe un bono para el jugador en ese tipo de reserva
            if (bonoExistente != null) {
                // Si ya tiene un bono, mostrar mensaje de error
                request.setAttribute("error", "Ya tienes un bono de este tipo.");
            } else {
                // Si no tiene bono para ese tipo de reserva, proceder a crear uno nuevo
                BonoDTO bonoCreado = gestorReservas.crearBono(correoJugador, tipoReserva);
                if (bonoCreado != null) {
                    // Si el bono se crea con éxito, mostrar mensaje de éxito
                    request.setAttribute("success", "¡Bono creado exitosamente!");
                    request.setAttribute("bono", bonoCreado);
                } else {
                    // Si hubo un error al crear el bono, mostrar mensaje de error
                    request.setAttribute("error", "Hubo un error al crear el bono. Intente nuevamente.");
                }
            }

        } catch (Exception e) {
            // Manejo de excepciones genéricas para cualquier error inesperado
            request.setAttribute("error", "Ocurrió un error inesperado: " + e.getMessage());
        }

        // Redirigir a la vista de adquisición de bonos
        request.getRequestDispatcher("/mvc/view/client/adquirirBonoView.jsp").forward(request, response);
    }
}
