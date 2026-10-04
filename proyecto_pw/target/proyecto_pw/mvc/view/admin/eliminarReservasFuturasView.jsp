<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ProyectoPW.src.dto.PistaDTO" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Eliminar Reservas Futuras</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/eliminarReservaFutura.css"> 

</head>
<body>
    <div class="container">
        <h1>Eliminar Reservas Futuras</h1>

        <!-- Mostrar mensajes de error, éxito o información -->
        <div class="messages">
            <% 
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");

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
        </div>

        <!-- Formulario para eliminar una reserva específica o todas las reservas -->
        <form action="${pageContext.request.contextPath}/EliminarReservasFuturasServlet" method="POST">
            <label for="accion">Selecciona acción:</label><br>
            <!-- Eliminar reserva específica -->
            <div class="alinear-radio-label">
                <input type="radio" id="especifica" name="accion" value="especifica" required>
                <label for="especifica">Eliminar reserva específica</label>
            </div>

            <!-- Eliminar todas las reservas futuras -->
            <div class="alinear-radio-label">
                <input type="radio" id="todas" name="accion" value="todas">
                <label for="todas">Eliminar todas las reservas futuras</label>
            </div>

            <!-- Campos para eliminar una reserva específica -->
            <div id="especificaFields" style="display:none;">
                <label for="nombrePista">Nombre de la pista:</label><br>
                <input type="text" id="nombrePista" name="nombrePista"><br><br>

                <label for="fechaReserva">Fecha de reserva (Formato: aaaa-mm-dd hh:mm):</label><br>
                <input type="datetime-local" id="fechaReserva" name="fechaReserva"><br><br>
            </div>

            <button type="submit">Eliminar</button>
        </form>

        <!-- Volver a la página anterior -->
        <a href="<%= request.getContextPath() %>/mvc/view/admin/mainAdminView.jsp">Volver</a>
        
        <script>
            // Mostrar u ocultar los campos de reserva específica según la opción seleccionada
            const radioEspecifica = document.getElementById('especifica');
            const radioTodas = document.getElementById('todas');
            const especificaFields = document.getElementById('especificaFields');

            radioEspecifica.addEventListener('change', function() {
                especificaFields.style.display = 'block';
            });

            radioTodas.addEventListener('change', function() {
                especificaFields.style.display = 'none';
            });
        </script>
    </div>
</body>
</html>
