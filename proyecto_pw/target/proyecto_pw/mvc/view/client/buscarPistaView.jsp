<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="ProyectoPW.src.dto.PistaDTO" %>

<%
    // Obtener la lista de pistas desde la request
    List<PistaDTO> pistas = (List<PistaDTO>) request.getAttribute("pistas");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Buscar Pistas Disponibles</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/buscarPistaView.css">
</head>
<body>
    <h1>Buscar Pistas Disponibles</h1>

    <!-- Formulario para buscar pistas -->
    <form action="${pageContext.request.contextPath}/BuscarPistaServlet" method="GET">
        <label for="esExterior">Tipo de pista:</label>
        <select name="esExterior" required>
            <option value="true">Exterior</option>
            <option value="false">Interior</option>
        </select>

        <label for="fecha">Fecha:</label>
        <input type="date" name="fecha" required>

        <button type="submit">Buscar</button>
        
        
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
            <div class="error-message">
                <%= error %>
            </div>
        <% } %>
        <% if (success != null) { %>
            <div class="success-message">
                <%= success %>
            </div>
        <% } %>
        <% if (info != null) { %>
            <div class="info-message">
                <%= info %>
            </div>
        <% } %>
    </div>

    <!-- Mostrar resultados de búsqueda -->
    <h2>Resultados de la búsqueda</h2>
    <ul>
        <% if (pistas != null && !pistas.isEmpty()) { %>
            <% for (PistaDTO pista : pistas) { %>
                <li>
                    <strong>Nombre:</strong> <%= pista.getNombre() %> <br>
                    <strong>Disponible:</strong> <%= pista.isDisponible() ? "Sí" : "No" %> <br>
                    <strong>Tipo de pista:</strong> <%= pista.isEsExterior() ? "Exterior" : "Interior" %> <br>
                    <strong>Materiales:</strong> 
                    <% List<?> materiales = pista.getMateriales();
                       if (materiales != null && !materiales.isEmpty()) { %>
                        <ul>
                            <% for (Object material : materiales) { %>
                                <li><%= material.toString() %></li>
                            <% } %>
                        </ul>
                    <% } else { %>
                        Ninguno
                    <% } %>
                </li>
            <% } %>
        <% } else { %>
            <p>No se encontraron pistas disponibles con los criterios especificados.</p>
        <% } %>
    </ul>
</body>
</html>
