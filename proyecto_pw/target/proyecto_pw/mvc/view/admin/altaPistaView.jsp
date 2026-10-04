<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ProyectoPW.src.dao.PistaDAO, ProyectoPW.src.dto.PistaDTO" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alta de Pista</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/altaPistaView.css"> <!-- Enlace a tu archivo CSS si tienes uno -->
</head>
<body>
    <div class="container">
        <h1>Dar de Alta Pista</h1>
        
        <!-- Mostrar mensajes de éxito o error -->
        <%
            String error = (String) request.getAttribute("error");
            String success = (String) request.getAttribute("success");
            if (error != null) {
        %>
            <div class="alert error"><%= error %></div>
        <% 
            }
            if (success != null) {
        %>
            <div class="alert success"><%= success %></div>
        <% 
            }
        %>

        <!-- Formulario para crear una pista -->
        <form action="<%= request.getContextPath() %>/admin/alta" method="POST">
            <input type="hidden" name="tipo" value="pista"> <!-- Añadido para identificar el tipo de formulario -->
            <label for="nombre">Nombre de la Pista:</label>
            <input type="text" name="nombre" id="nombre">
            <br><br>
        
            <label for="disponible">Disponible:</label>
            <input type="checkbox" name="disponible" id="disponible" value="true">
            <br><br>
        
            <label for="esExterior">Exterior:</label>
            <input type="checkbox" name="esExterior" id="esExterior" value="true">
            <br><br>
        
            <label for="tamano">Tamaño de la Pista:</label>
            <select name="tamano" id="tamano">
                <option value="MINIBASKET">Minibasket</option>
                <option value="ADULTOS">Adultos</option>
                <option value="TRES_VS_TRES">3 vs 3</option>
            </select>
            <br><br>
        
            <label for="maxJugadores">Máximo de Jugadores:</label>
            <input type="number" name="maxJugadores" id="maxJugadores" min="0" required>
            <br><br>
        
            <button type="submit">Registrar Pista</button>


            <a href="<%= request.getContextPath() %>/mvc/view/admin/mainAdminView.jsp">Volver</a>            
        </form>
    </div>
</body>
</html>
