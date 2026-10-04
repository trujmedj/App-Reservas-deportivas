<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ProyectoPW.src.dao.PistaDAO, ProyectoPW.src.dto.PistaDTO" %>
<%@ page import="java.util.List" %>

<%
    List<PistaDTO> listaPistas = (List<PistaDTO>) request.getAttribute("pistasDisponibles");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Realizar Reserva</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/realizarReservaView.css"> <!-- Enlace a tu archivo CSS -->
</head>
<body>
    <div class="container">
        <h1>Realizar Reserva</h1>

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

        <form action="<%= request.getContextPath() %>/RealizarReservaServlet" method="POST">
            
            <label for="fechaHora">Fecha y Hora de la Reserva:</label>
            <input type="datetime-local" name="fechaHora" id="fechaHora" required><br><br>

            <label for="duracion">Duración:</label>
            <select name="duracion" id="duracion" required>
                <option value="60">60 minutos</option>
                <option value="90">90 minutos</option>
                <option value="120">120 minutos</option>
            </select><br><br>

            <label for="pista">Seleccionar Pista:</label>
            <select name="pista" id="pista" required>
                <%
                    if (listaPistas != null && !listaPistas.isEmpty()) {
                        for (PistaDTO pista : listaPistas) {
                %>
                        <option value="<%= pista.getNombre() %>"><%= pista.getNombre() %> - <%= pista.getTamano() %></option>
                <%
                        }
                    } else {
                %>
                        <option>No hay pistas disponibles</option>
                <%
                    }
                %>
            </select><br><br>

            <!-- Número de Adultos -->
            <label for="numAdultos">Número de Adultos:</label>
            <input type="number" name="numAdultos" id="numAdultos" min="0" required 
                   oninvalid="this.setCustomValidity('El número de adultos no puede ser negativo.')"
                   oninput="this.setCustomValidity('')"><br><br>

            <!-- Número de Niños -->
            <label for="numNinos">Número de Niños:</label>
            <input type="number" name="numNinos" id="numNinos" min="0" required 
                   oninvalid="this.setCustomValidity('El número de niños no puede ser negativo.')"
                   oninput="this.setCustomValidity('')"><br><br>

            <button type="submit">Realizar Reserva</button>
        </form>

        <!-- Botón para volver a la vista principal -->
        
        <a href="<%= request.getContextPath() %>/mvc/view/client/mainClientView.jsp">Volver</a>
        
    </div>
</body>
</html>
