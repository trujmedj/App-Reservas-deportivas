<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ProyectoPW.src.dao.MaterialDAO, ProyectoPW.src.dto.MaterialDTO" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alta de Material</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/altaMaterialView.css"> <!-- Asegúrate de que el archivo CSS esté en esta ruta -->
</head>
<body>
    <div class="container">
        <h1>Dar de Alta Material</h1>

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

        <!-- Formulario para crear un material -->
        <form action="<%= request.getContextPath() %>/admin/alta" method="POST">
            <input type="hidden" name="tipo" value="material"> <!-- Identificador del tipo de formulario -->

            <label for="tipoMaterial">Tipo de Material:</label>
            <select name="tipoMaterial" id="tipoMaterial">
                <option value="PELOTAS">Pelotas</option>
                <option value="CANASTAS">Canastas</option>
                <option value="CONOS">Conos</option>
            </select>

            <label for="usoExterior">Uso Exterior:</label>
            <input type="checkbox" name="usoExterior" id="usoExterior" value="true">

            <label for="estado">Estado:</label>
            <select name="estado" id="estado">
                <option value="DISPONIBLE">Disponible</option>
                <option value="RESERVADO">Reservado</option>
                <option value="MAL_ESTADO">Mal Estado</option>
            </select>

            <button type="submit">Registrar Material</button>


            <a href="<%= request.getContextPath() %>/mvc/view/admin/mainAdminView.jsp">Volver</a>            
        </form>
    </div>
</body>
</html>

