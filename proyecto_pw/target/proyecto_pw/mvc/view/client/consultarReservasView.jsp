<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="ProyectoPW.src.dto.ReservaDTO" %>

<%
    // Obtener las listas de reservas desde la request
    List<ReservaDTO> reservasFuturas = (List<ReservaDTO>) request.getAttribute("reservasFuturas");
    List<ReservaDTO> reservasFinalizadas = (List<ReservaDTO>) request.getAttribute("reservasFinalizadas");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reservas Consultadas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/consultarReservasView.css">
</head>
<body>
    <h1>Consultar Reservas</h1>

    <!-- Formulario para ingresar las fechas -->
    <form action="${pageContext.request.contextPath}/ConsultarReservasServlet" method="GET">
        <label for="fechaInicio">Fecha de Inicio:</label>
        <input type="date" name="fechaInicio" required>
        <label for="fechaFin">Fecha de Fin:</label>
        <input type="date" name="fechaFin" required>
        <button type="submit">Consultar</button>

        <a href="<%= request.getContextPath() %>/mvc/view/client/mainClientView.jsp">Volver</a>
    </form>

    <!-- Mostrar mensajes de error, éxito o información -->
    <div class="messages">
        <% 
            String error = (String) request.getAttribute("error");
            String success = (String) request.getAttribute("success");
            String info = (String) request.getAttribute("info");

            if (error != null) { 
        %>
            <div class="error-message"><%= error %></div>
        <% } %>
        <% if (success != null) { %>
            <div class="success-message"><%= success %></div>
        <% } %>
        <% if (info != null) { %>
            <div class="info-message"><%= info %></div>
        <% } %>
    </div>

    <!-- Mostrar reservas -->
    <h2>Reservas Futuras</h2>
    <ul>
        <% if (reservasFuturas != null && !reservasFuturas.isEmpty()) { %>
            <% for (ReservaDTO reserva : reservasFuturas) { %>
                <li>
                    <strong>Fecha:</strong> <%= reserva.getFechaHora() %> <br>
                    <strong>Pista:</strong> <%= reserva.getIdPista() %> <br>
                    <strong>Precio:</strong> <%= reserva.getPrecio() %>
                </li>
            <% } %>
        <% } else { %>
            <p>No hay reservas futuras.</p>
        <% } %>
    </ul>

    <h2>Reservas Finalizadas</h2>
    <ul>
        <% if (reservasFinalizadas != null && !reservasFinalizadas.isEmpty()) { %>
            <% for (ReservaDTO reserva : reservasFinalizadas) { %>
                <li>
                    <strong>Fecha:</strong> <%= reserva.getFechaHora() %> <br>
                    <strong>Pista:</strong> <%= reserva.getIdPista() %> <br>
                    <strong>Precio:</strong> <%= reserva.getPrecio() %>
                </li>
            <% } %>
        <% } else { %>
            <p>No hay reservas finalizadas.</p>
        <% } %>
    </ul>
</body>
</html>
