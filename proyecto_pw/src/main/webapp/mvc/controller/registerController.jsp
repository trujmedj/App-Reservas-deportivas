<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ProyectoPW.src.dao.JugadorDAO, ProyectoPW.src.dto.JugadorDTO" %>
<%@ page import="java.util.Date, java.text.SimpleDateFormat" %>

<jsp:useBean id="usuarioBean" scope="session" class="ProyectoPW.src.display.UsuarioBean"></jsp:useBean>

<%
String nextPage = "../../index.jsp";
String mensajeNextPage = "";

// Comprobamos si hay parámetros del formulario de registro
String name = request.getParameter("name");
String surname = request.getParameter("surname");
String birthdateStr = request.getParameter("birthdate");
String email = request.getParameter("email");
String password = request.getParameter("password");
String role = request.getParameter("role"); // "jugador" o "admin"

// Fecha actual para inscripción
Date fechaInscripcion = new Date();
    

if (name != null && surname != null && birthdateStr != null && email != null && password != null && role != null) {
    
    try {
        // Convertir la fecha de nacimiento de String a Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthdate = dateFormat.parse(birthdateStr);

        // Crear nuevo JugadorDTO con los datos del formulario
        JugadorDTO nuevoJugador = new JugadorDTO(
            name,
            surname,
            birthdate,
            fechaInscripcion,
            email,
            password,
            role
        );

        // Usar JugadorDAO para intentar registrar al jugador
        JugadorDAO jugadorDAO = new JugadorDAO();

        if (jugadorDAO.agregarJugador(nuevoJugador)) {
            mensajeNextPage = "Registro completado. Por favor, inicia sesión.";
        } else {
            nextPage = "../view/acceso/registerView.jsp";
            mensajeNextPage = "El usuario ya existe. Por favor, inténtelo de nuevo.";
        }
    } catch (Exception e) {
        nextPage = "../view/acceso/registerView.jsp";
        mensajeNextPage = "Error en los datos ingresados. Por favor, inténtelo de nuevo.";
    }
} else {
    // Sin parámetros: redirigimos al formulario de registro
    nextPage = "../view/acceso/registerView.jsp";
}
%>
<jsp:forward page="<%=nextPage%>">
    <jsp:param name="message" value="<%=mensajeNextPage%>" />
</jsp:forward>

