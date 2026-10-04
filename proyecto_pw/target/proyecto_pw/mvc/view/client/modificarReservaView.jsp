<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="ProyectoPW.src.dto.ReservaDTO" %>
<%@ page import="ProyectoPW.src.dto.PistaDTO" %>

<%
    List<ReservaDTO> reservasModificables = (List<ReservaDTO>) request.getAttribute("reservasModificables");
    List<PistaDTO> pistasDisponibles = (List<PistaDTO>) request.getAttribute("pistasDisponibles");
    String error = (String) request.getAttribute("error");
    String mensaje = (String) request.getAttribute("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modificar Reserva</title>
   <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modificarReservaView.css">
</head>
<body>
    <div class="container">
        <h1>Modificar Reserva</h1>

        <!-- Mostrar mensajes de error o éxito -->
        <% if (error != null) { %>
            <div class="alert error"><%= error %></div>
        <% } if (mensaje != null) { %>
            <div class="alert success"><%= mensaje %></div>
        <% } %>

        <!-- Formulario de modificación de reserva -->
        <form action="<%= request.getContextPath() %>/ModificarReservaServlet" method="POST">
            <!-- Selección de reserva -->
            <label for="reservaSeleccionada">Seleccione una Reserva a Modificar:</label>
            <select name="reservaSeleccionada" id="reservaSeleccionada" required>
                <option value="" disabled selected>Seleccione una reserva</option>
                <% if (reservasModificables != null && !reservasModificables.isEmpty()) {
                    for (ReservaDTO reserva : reservasModificables) { %>
                        <option value="<%= reserva.getFechaHora() %>">
                            <%= "Fecha: " + reserva.getFechaHora() + ", Pista: " + reserva.getIdPista() %>
                        </option>
                <% }} else { %>
                    <option>No hay reservas disponibles</option>
                <% } %>
            </select><br><br>

            <!-- Modificación de datos -->
            <div id="modificacionReserva">
                <label for="nuevaPista">Seleccione la Pista:</label>
                <select name="nuevaPista" id="nuevaPista" required>
                    <option value="" disabled selected>Seleccione una pista</option>
                    <% if (pistasDisponibles != null && !pistasDisponibles.isEmpty()) {
                        for (PistaDTO pista : pistasDisponibles) { %>
                            <option value="<%= pista.getNombre() %>"><%= pista.getNombre() %></option>
                    <% }} else { %>
                        <option>No hay pistas disponibles</option>
                    <% } %>
                </select><br><br>

                <label for="nuevaFechaYHora">Fecha y Hora de la Reserva:</label>
                <input type="datetime-local" name="nuevaFechaYHora" id="nuevaFechaYHora" required><br><br>

                <label for="nuevaDuracion">Duración (en minutos):</label>
                <select name="nuevaDuracion" id="nuevaDuracion" required>
                    <option value="" disabled selected>Seleccione la duración</option>
                    <option value="60">60 minutos</option>
                    <option value="90">90 minutos</option>
                    <option value="120">120 minutos</option>
                </select><br><br>


                <label for="numAdultos">Número de Adultos:</label>
                <input type="number" name="numAdultos" id="numAdultos" min="0" required 
                    oninvalid="this.setCustomValidity('El número de adultos no puede ser negativo.')"
                    oninput="this.setCustomValidity('')"><br><br>

                <!-- Número de Niños -->
                <label for="numNinos">Número de Niños:</label>
                <input type="number" name="numNinos" id="numNinos" min="0" required 
                    oninvalid="this.setCustomValidity('El número de niños no puede ser negativo.')"
                    oninput="this.setCustomValidity('')"><br><br>

                <button type="submit">Modificar Reserva</button>
            </div>
        </form>

        <!-- Enlace para volver -->
        <a href="<%= request.getContextPath() %>/mvc/view/client/mainClientView.jsp" class="volver">Volver</a>
    </div>

    <script>
        // Función que controla la lógica al seleccionar una reserva
        document.getElementById('reservaSeleccionada').addEventListener('change', function() {
            var reservaSeleccionada = this.value;
            document.getElementById('modificacionReserva').style.display = reservaSeleccionada ? 'block' : 'none';
        });

        // Inicialmente ocultar los campos de modificación
        document.getElementById('modificacionReserva').style.display = 'none';
    </script>
</body>
</html>
