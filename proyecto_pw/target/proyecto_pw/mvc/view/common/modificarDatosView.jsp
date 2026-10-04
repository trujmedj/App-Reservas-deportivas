<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ProyectoPW.src.dto.JugadorDTO" %>

<jsp:useBean id="jugador" scope="request" class="ProyectoPW.src.dto.JugadorDTO"></jsp:useBean>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modificar Datos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modificarDatosView.css">
    <!-- Enlace al script externo -->
    <script src="${pageContext.request.contextPath}/js/mostrarContrasena.js"></script>
</head>
<body>
    <div class="container">
        <h1>Modificar Datos</h1>
        <form action="<%= request.getContextPath() %>/mvc/controller/guardarDatosController.jsp" method="post">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" value="<%= jugador.getNombre() %>"><br>

            <label for="apellidos">Apellidos:</label>
            <input type="text" id="apellidos" name="apellidos" value="<%= jugador.getApellidos() %>"><br>

            <label for="contrasena">Nueva Contraseña:</label>
            <input type="password" id="password" name="contrasena"><br>

            <!-- Checkbox para mostrar/ocultar contraseña -->
            <div class="form-group">
                <input type="checkbox" id="showPassword" onclick="togglePasswordVisibility()">
                <label for="showPassword">Mostrar contraseña</label>
            </div>

            <button type="submit">Guardar Cambios</button>
            
            <a href="javascript:history.back()">Volver</a>
        </form>
    </div>
</body>
</html>
