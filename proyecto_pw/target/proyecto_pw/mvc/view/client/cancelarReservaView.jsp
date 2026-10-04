<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="ProyectoPW.src.dto.ReservaDTO" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestionar Reservas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cancelarReservaView.css">
</head>
<body>
    <!-- Contenedor principal -->
    <div class="container">
        <h1>Gestionar Reservas</h1>

        <% 
            // Mostrar mensajes según los atributos configurados en el request
            String error = (String) request.getAttribute("error");
            String success = (String) request.getAttribute("success");
            if (error != null) {
        %>
            <p style="color: red;"><%= error %></p>
        <% 
            } 
            if (success != null) {
        %>
            <p style="color: green;"><%= success %></p>
        <% 
            } 
        %>

        <h2>Mis reservas cancelables</h2>

        <form action="<%= request.getContextPath() %>/CancelarReservaServlet" method="post">
            <label for="fechaHoraReserva">Seleccione una reserva para cancelar:</label>
            <select name="fechaHoraReserva" id="fechaHoraReserva" required>
                <option value="" disabled selected>-- Seleccionar reserva --</option>

                <% 
                    // Mostrar las reservas modificables del usuario
                    List<ReservaDTO> reservas = (List<ReservaDTO>) request.getAttribute("reservas");
                    if (reservas != null && !reservas.isEmpty()) {
                        for (ReservaDTO reserva : reservas) {
                            String fechaHoraFormatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reserva.getFechaHora());
                %>
                            <option value="<%= fechaHoraFormatted %>">
                                <%= reserva.getIdPista() %> - <%= fechaHoraFormatted %> - Precio: <%= reserva.getPrecio() %> €
                            </option>
                <% 
                        }
                    } else {
                %>
                        <option value="" disabled>No tienes reservas modificables.</option>
                <% 
                    }
                %>
            </select>
            <br><br>
            <button type="submit">Cancelar Reserva</button>
        </form>

        <br><br>
        
        <a href="<%= request.getContextPath() %>/mvc/view/client/mainClientView.jsp">Volver</a>
    </div> <!-- Cierre del contenedor -->
</body>
</html>
