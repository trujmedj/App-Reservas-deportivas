package ProyectoPW.src.servlets.common;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ProyectoPW.src.display.UsuarioBean;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filtro de control de acceso que valida si el usuario tiene permisos para acceder a ciertas rutas,
 * dependiendo de si es administrador o cliente.
 * El filtro intercepta todas las solicitudes y verifica si el usuario está autenticado y autorizado
 * para acceder a las páginas correspondientes.
 */
@WebFilter("/*") // Filtra todas las solicitudes HTTP
public class AccessControlFilterServlet implements Filter {

    /**
     * Listado de rutas protegidas solo accesibles por administradores.
     */
    private static final List<String> ADMIN_ROUTES = Arrays.asList(
        "/mvc/view/admin/altaMaterialView.jsp",
        "/mvc/view/admin/altaPistaView.jsp",
        "/mvc/view/admin/asociarMaterialPistaView.jsp",
        "/mvc/view/admin/eliminarRerservasFuturasView.jsp",
        "/mvc/view/admin/mainAdminView.jsp",
        "/mvc/view/admin/modificarEstadoView.jsp",
        "/mvc/view/common/modificarDatosView.jsp",
        "/AltaServlet",
        "/AsociarMaterialServlet",
        "/EliminarReservasFuturasServlet",
        "/ModificarEstadoServlet"
    );

    /**
     * Listado de rutas protegidas solo accesibles por clientes.
     */
    private static final List<String> CLIENT_ROUTES = Arrays.asList(
        "/mvc/view/client/adquirirBonoView.jsp",
        "/mvc/view/client/buscarPistaView.jsp",
        "/mvc/view/client/cancelarReservaView.jsp",
        "/mvc/view/client/consultarReservasView.jsp",
        "/mvc/view/client/mainClientView.jsp",
        "/mvc/view/client/modificarReservaView.jsp",
        "/mvc/view/client/realizarReservaBonoView.jsp", 
        "/mvc/view/common/modificarDatosView.jsp",
        "/mvc/view/client/realizarReservaView.jsp",
        "/AdquirirBonoServlet",
        "/BuscarPistaServlet",
        "/CancelarReservaServlet",
        "/ConsultarReservasServlet",
        "/ModificarReservaServlet",
        "/RealizarReservaBonoServlet",
        "/RealizarReservaServlet"
    );

    /**
     * Método que filtra las solicitudes HTTP y valida el acceso a las rutas protegidas.
     * Verifica si el usuario está autenticado y autorizado según su rol (administrador o cliente).
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que se enviará al cliente.
     * @param chain La cadena de filtros para continuar con el procesamiento.
     * @throws IOException Si ocurre un error de entrada/salida durante el filtrado.
     * @throws ServletException Si ocurre un error en el procesamiento del servlet.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // Convertir el request y el response a los tipos específicos de HTTP
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Obtener la sesión actual
        HttpSession session = httpRequest.getSession(false);
        UsuarioBean usuarioBean = (session != null) ? (UsuarioBean) session.getAttribute("usuarioBean") : null;

        // Obtener la URL solicitada
        String requestURI = httpRequest.getRequestURI();
        
        // Verificar si la ruta es de administrador
        boolean esRutaAdmin = false;
        for (String ruta : ADMIN_ROUTES) {
            if (requestURI.contains(ruta)) {
                esRutaAdmin = true;
                break;
            }
        }

        if (esRutaAdmin) {
            // Si la ruta es de administrador, verificar si el usuario es un administrador
            if (usuarioBean == null || !usuarioBean.esAdministrador()) {
                // Si no es un administrador, redirigir a la página de login con un mensaje de acceso denegado
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/mvc/view/loginView.jsp?message=Acceso+denegado");
                return;
            }
        }

        // Verificar si la ruta es de cliente
        boolean esRutaCliente = false;
        for (String ruta : CLIENT_ROUTES) {
            if (requestURI.contains(ruta)) {
                esRutaCliente = true;
                break;
            }
        }

        if (esRutaCliente) {
            // Si la ruta es de cliente, verificar si el usuario es un cliente
            if (usuarioBean == null || !usuarioBean.esJugador()) {
                // Si no es un cliente, redirigir a la página de login con un mensaje de acceso denegado
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/mvc/view/loginView.jsp?message=Acceso+denegado");
                return;
            }
        }

        // Si el usuario tiene acceso a la ruta, continuar con la cadena de filtros o servlets
        chain.doFilter(request, response);
    }
}

