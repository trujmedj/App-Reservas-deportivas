<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ProyectoPW.src.dto.JugadorDTO" %>

<jsp:useBean id="usuarioBean" scope="session" class="ProyectoPW.src.display.UsuarioBean"></jsp:useBean>

<%
    // Obtener el jugador desde el bean de sesión
    JugadorDTO jugador = usuarioBean.getJugador();

    // Redirigir al formulario de modificación de datos
    if (jugador != null) {
        request.setAttribute("jugador", jugador);
        request.getRequestDispatcher("../view/common/modificarDatosView.jsp").forward(request, response);
    } else {
        // Si no hay un jugador en sesión, redirigir al inicio de sesión
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
%>
