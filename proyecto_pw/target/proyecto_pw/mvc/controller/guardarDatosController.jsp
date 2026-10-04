<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ProyectoPW.src.dao.JugadorDAO, ProyectoPW.src.dto.JugadorDTO" %>

<jsp:useBean id="usuarioBean" scope="session" class="ProyectoPW.src.display.UsuarioBean"></jsp:useBean>

<%
    /**
     * Obtiene el objeto jugador de la sesión y actualiza su información
     * con los valores proporcionados a través de un formulario HTML. 
     * 
     * <p>Si la información se actualiza correctamente en la base de datos, 
     * redirige al usuario según su rol. En caso contrario, redirige a una 
     * página de error. Si no hay un jugador en la sesión, redirige al inicio
     * de sesión.</p>
     */

    // Obtener el jugador desde el bean de sesión
    JugadorDTO jugador = usuarioBean.getJugador();

    if (jugador != null) {
        /**
         * Obtiene los nuevos valores enviados desde el formulario a través
         * de los parámetros de la solicitud HTTP.
         * 
         * @param nombre Nuevo nombre del jugador.
         * @param apellidos Nuevos apellidos del jugador.
         * @param contrasena Nueva contraseña del jugador (opcional).
         */
        String nuevoNombre = request.getParameter("nombre");
        String nuevosApellidos = request.getParameter("apellidos");
        String nuevaContrasena = request.getParameter("contrasena");

        /**
         * Actualiza los valores del objeto jugador con los datos recibidos
         * del formulario. Si se proporciona una nueva contraseña, también
         * se actualiza en el objeto.
         */
        jugador.setNombre(nuevoNombre);
        jugador.setApellidos(nuevosApellidos);
        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
            jugador.setContrasena(nuevaContrasena);
        }

        /**
         * Crea una instancia de la clase JugadorDAO para realizar operaciones
         * con la base de datos. Intenta actualizar los datos del jugador.
         * 
         * @return actualizado Indica si la actualización fue exitosa o no.
         */
        JugadorDAO jugadorDAO = new JugadorDAO();
        boolean actualizado = jugadorDAO.modificarJugador(jugador, jugador.getCorreo());

        if (actualizado) {
            /**
             * Si la actualización fue exitosa, actualiza el bean de sesión
             * y redirige al usuario según su rol:
             * 
             * - Administrador: Página principal del administrador.
             * - Jugador: Página principal del cliente.
             */
            usuarioBean.setJugador(jugador);

            if (usuarioBean.esAdministrador()) {
                response.sendRedirect("../view/admin/mainAdminView.jsp");
            } else if (usuarioBean.esJugador()) {
                response.sendRedirect("../view/client/mainClientView.jsp");
            }
        } else {
            /**
             * Redirige a una página de error si no se pudo actualizar 
             * la información del jugador en la base de datos.
             */
            response.sendRedirect("../view/errorView.jsp");
        }
    } else {
        /**
         * Si no hay un jugador activo en la sesión, redirige al usuario
         * a la página de inicio de sesión.
         */
        response.sendRedirect("../../index.jsp");
    }
%>
