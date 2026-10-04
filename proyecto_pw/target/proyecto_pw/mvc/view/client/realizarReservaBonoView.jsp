<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="ProyectoPW.src.dto.BonoDTO, ProyectoPW.src.dto.PistaDTO" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hacer Reserva con Bono</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/realizarReservaBono.css">
</head>
<body>
    <div class="container">
        <h1>Reservar Pista con Bono</h1>

        <!-- Mensajes de error y éxito -->
        <%
        String error = (String) session.getAttribute("error");
        String success = (String) session.getAttribute("success");
        if (error != null) {
        %>
            <p class="error"><%= error %></p>
        <%
                session.removeAttribute("error");
            }
            if (success != null) {
        %>
            <p class="success"><%= success %></p>
        <%
                session.removeAttribute("success");
            }
        %>

        <!-- Mostrar los bonos del jugador -->
        <h2>Bonos Disponibles</h2>
        <%
        List<BonoDTO> bonos = (List<BonoDTO>) request.getAttribute("bonos");
        if (bonos == null || bonos.isEmpty()) {
        %>
            <p>No se encontraron bonos asociados a este jugador.</p>
        <%
        } else {
        %>
            <table class="table">
        <thead>
            <tr>
                <th>Tipo de Bono</th>
                <th>Fecha de Expiración</th>
                <th>Créditos Restantes</th>
            </tr>
        </thead>
        <tbody>
            <%
                for (BonoDTO bono : bonos) {
            %>
            <tr>
                <td><%= bono.getTipoReserva() %></td>
                <td><%= bono.getFechaCaducidad() %></td>
                <td><%= bono.getSesionesRestantes() %></td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
        <%
        }
        %>

        <!-- Formulario para realizar la reserva -->
        <h2>Realizar Reserva</h2>
        <form action="RealizarReservaBonoServlet" method="post">
            <!-- Selección de bono -->
            <label for="bonoSeleccionado">Seleccione un bono:</label><br>
            <select id="bonoSeleccionado" name="bonoSeleccionado" required>
                <option value="">-- Seleccione --</option>
                <%
                    if (bonos != null) {
                        for (BonoDTO bono : bonos) {
                %>
                    <option value="<%= bono.getTipoReserva() %>">
                        <%= bono.getTipoReserva() %> - <%= bono.getIdBono() %> - Créditos restantes: <%= bono.getSesionesRestantes() %>
                    </option>
                <%
                        }
                    }
                %>
            </select><br><br>

            <!-- Ingreso de fecha y hora -->
            <label for="fechaHora">Fecha y hora de la reserva:</label><br>
            <input type="datetime-local" id="fechaHora" name="fechaHora" required><br><br>

            <!-- Duración -->
            <label for="duracion">Duración de la reserva (60, 90 o 120 minutos):</label><br>
            <input type="number" id="duracion" name="duracion" min="60" max="120" step="30" required><br><br>

            <!-- Selección de pista -->
            <label for="pista">Seleccione una pista:</label><br>
            <select id="pista" name="pista" required>
                <option value="">-- Seleccione --</option>
                <%
                    List<PistaDTO> pistas = (List<PistaDTO>) request.getAttribute("pistas");
                    if (pistas != null) {
                        for (PistaDTO pista : pistas) {
                %>
                    <option value="<%= pista.getNombre() %>"><%= pista.getNombre() %></option>
                <%
                        }
                    }
                %>
            </select><br><br>

            <!-- Número de adultos y niños -->
            <label for="numAdultos">Número de adultos:</label><br>
            <input type="number" id="numAdultos" name="numAdultos" min="0" required><br><br>

            <label for="numNinos">Número de niños:</label><br>
            <input type="number" id="numNinos" name="numNinos" min="0" required><br><br>

            <!-- Botón para reservar -->
            <button type="submit">Reservar</button>
        </form>

        <!--Boton de volver-->
        <form>
            <a href="<%= request.getContextPath() %>/mvc/view/client/mainClientView.jsp">Volver</a>
        </form>
    </div>

    
</body>
</html>
