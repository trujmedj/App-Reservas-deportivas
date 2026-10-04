<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ProyectoPW.src.dao.JugadorDAO, ProyectoPW.src.dto.JugadorDTO" %>

<jsp:useBean id="usuarioBean" scope="session" class="ProyectoPW.src.display.UsuarioBean"></jsp:useBean>

<%
    String nextPage = "../../index.jsp";
    String mensajeNextPage = "";
    
    // Obtener los parámetros del formulario de inicio de sesión
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    
    // Comprobamos si se han enviado las credenciales
    if (email != null && password != null) {
        JugadorDAO jugadorDAO = new JugadorDAO();
    
        // Verificar las credenciales
        if (jugadorDAO.verificarUsuario(email, password)) {
            // Recuperar información completa del usuario
            JugadorDTO jugadorLogado = jugadorDAO.buscarJugadorPorCorreo(email);
    
            if (jugadorLogado != null) {
                // Establecer el usuario en el bean de sesión
                usuarioBean.setJugador(jugadorLogado);
                mensajeNextPage = "Inicio de sesión exitoso.";
    
                // Verificar el rol del usuario y redirigir según corresponda
                if (usuarioBean.esAdministrador()) {
                    nextPage = "../view/admin/mainAdminView.jsp";  // Redirigir a la página del administrador
                } else if (usuarioBean.esJugador()) {
                    nextPage = "../view/client/mainClientView.jsp";  // Redirigir a la página del cliente/jugador
                } else {
                    nextPage = "../view/acceso/loginView.jsp";  // En caso de que no sea un rol válido
                    mensajeNextPage = "Rol no reconocido.";
                }
            } else {
                nextPage = "../view/acceso/loginView.jsp";
                mensajeNextPage = "Error al recuperar la información del usuario.";
            }
        } else {
            nextPage = "../view/acceso/loginView.jsp";
            mensajeNextPage = "Correo electrónico o contraseña incorrectos.";
        }
    } else {
        nextPage = "../view/acceso/loginView.jsp";
        mensajeNextPage = "Por favor, introduzca sus credenciales.";
    }

    // Verificar que nextPage tiene una ruta válida antes de hacer el forward
    if (nextPage == null || nextPage.isEmpty()) {
        nextPage = "../view/acceso/loginView.jsp";  // Página predeterminada en caso de error
    }
%>

<jsp:forward page="<%=nextPage%>">
    <jsp:param name="message" value="<%=mensajeNextPage%>" />
</jsp:forward>

