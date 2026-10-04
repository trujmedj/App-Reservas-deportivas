<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="usuarioBean" scope="session" class="ProyectoPW.src.display.UsuarioBean"></jsp:useBean>

<%
    // Comprobar si el usuario está logado
    if (usuarioBean != null && usuarioBean.getCorreo() != null && !usuarioBean.getCorreo().equals("")) {
        // Redirigir al controlador de login si está logado
        response.sendRedirect("/proyecto_pw/mvc/controller/loginController.jsp");
        return; // Importante: evitar que el resto del código HTML se ejecute
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Gestión de Reservas</title>
    <link rel="stylesheet" type="text/css" href="css/index.css"> <!-- Asegúrate de vincular tu archivo CSS -->
</head>
<body>
    <div class="welcome-container">
        <h1>Bienvenido a Gestión de Reservas</h1>
        <a href="<%= request.getContextPath() %>/mvc/controller/loginController.jsp">Iniciar Sesión</a>
|
        <a href="<%= request.getContextPath() %>/mvc/controller/registerController.jsp">Registrarse</a>
    </div>
    <footer>
        <p>© 2024 Gestión de Reservas de Pistas de Baloncesto</p>
    </footer>
</body>
</html>