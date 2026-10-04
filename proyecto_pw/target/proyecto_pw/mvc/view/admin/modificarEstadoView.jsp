<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="ProyectoPW.src.dto.MaterialDTO, ProyectoPW.src.dao.MaterialDAO" %>
<%@ page import="ProyectoPW.src.dto.PistaDTO, ProyectoPW.src.dao.PistaDAO" %>
<%@ page import="ProyectoPW.src.dto.MaterialDTO.EstadoMaterial" %>
<%@ page import="ProyectoPW.src.dto.PistaDTO.TamanoPista" %>

<%
    // Crear los DAO para acceder a los datos
    MaterialDAO materialDAO = new MaterialDAO();
    PistaDAO pistaDAO = new PistaDAO();

    // Obtener la lista de los materiales
    List<MaterialDTO> listaMateriales = materialDAO.obtenerTodosLosMateriales();
    // Obtener la lista de las pistas
    List<PistaDTO> listaPistas = pistaDAO.obtenerTodasLasPistas();
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Modificar Estado</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modificarEstadoView.css">
</head>
<body>
    <div class="container">
        <h1>Modificar Estado de Materiales y Pistas</h1>

        <!-- Botón para volver a la vista principal -->
        <div class="common-actions">
            <a href="<%= request.getContextPath() %>/mvc/view/admin/mainAdminView.jsp">Volver</a>
        </div>

        <!-- Mostrar mensajes de error, éxito o información -->
        <div class="messages">
            <% 
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");

                if (error != null) { 
            %>
            <div class="error-message" style="color: red;">
                <%= error %>
            </div>
            <% } %>
            <% if (success != null) { %>
            <div class="success-message" style="color: green;">
                <%= success %>
            </div>
            <% } %>
        </div>

        <!-- Mostrar la lista de materiales -->
        <section aria-labelledby="material-list">
            <h2 id="material-list">Lista de Materiales</h2>
            <table border="1" class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tipo</th>
                        <th>Uso Exterior</th>
                        <th>Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (listaMateriales != null && !listaMateriales.isEmpty()) {
                            for (MaterialDTO material : listaMateriales) {
                                // Determinar si el material es de uso exterior
                                String usoExterior = material.isUsoExterior() ? "Sí" : "No";
                        %>
                        <tr>
                            <td class="material-id"><%= material.getIdMaterial() %></td>
                            <td class="material-type"><%= material.getTipo() %></td>
                            <td class="material-uso"><%= usoExterior %></td>
                            <td class="material-estado"><%= material.getEstado() %></td>
                        </tr>
                        <% 
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="4">No hay materiales registrados</td>
                    </tr>
                    <% 
                        }
                    %>
                </tbody>
            </table>
        </section>

        <!-- Formulario para modificar estado de un material -->
        <section aria-labelledby="modify-material">
            <h2 id="modify-material">Modificar Estado de Material</h2>
            <form action="${pageContext.request.contextPath}/ModificarEstadoServlet" method="POST">
                <label for="idMaterial">ID del Material:</label>
                <select name="idMaterial" required>
                    <% 
                        if (listaMateriales != null && !listaMateriales.isEmpty()) {
                            for (MaterialDTO material : listaMateriales) {
                    %>
                        <option value="<%= material.getIdMaterial() %>">
                            <%= material.getIdMaterial() %> - <%= material.getTipo() %>
                        </option>
                    <% 
                            }
                        } else {
                    %>
                        <option disabled>No hay materiales disponibles</option>
                    <% } %>
                </select>

                <label for="estadoMaterial">Nuevo Estado:</label>
                <select name="estadoMaterial" required>
                    <% for (EstadoMaterial estado : EstadoMaterial.values()) { %>
                        <option value="<%= estado %>"><%= estado %></option>
                    <% } %>
                </select>

                <input type="hidden" name="tipoObjeto" value="material">
                <button type="submit">Modificar Estado de Material</button>
            </form>
        </section>

        <hr>

        <!-- Mostrar la lista de pistas -->
        <section aria-labelledby="pista-list">
            <h2 id="pista-list">Lista de Pistas</h2>
            <table border="1" class="table">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Disponible</th>
                        <th>Exterior</th>
                        <th>Tamaño</th>
                        <th>Máx. Jugadores</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (listaPistas != null && !listaPistas.isEmpty()) {
                            for (PistaDTO pista : listaPistas) {
                                // Determinar si la pista es exterior y si está disponible
                                String disponible = pista.isDisponible() ? "Sí" : "No";
                                String exterior = pista.isEsExterior() ? "Sí" : "No";
                            %>
                            <tr>
                                <td class="pista-name"><%= pista.getNombre() %></td>
                                <td class="pista-disponible"><%= disponible %></td>
                                <td class="pista-exterior"><%= exterior %></td>
                                <td class="pista-tamano"><%= pista.getTamano() %></td>
                                <td class="pista-max-jugadores"><%= pista.getMaxJugadores() %></td>
                            </tr>
                            <% 
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="5">No hay pistas registradas</td>
                    </tr>
                    <% 
                        }
                    %>
                </tbody>
            </table>
        </section>

        <!-- Formulario para modificar estado de una pista -->
        <section aria-labelledby="modify-pista">
            <h2 id="modify-pista">Modificar Estado de Pista</h2>
            <form action="${pageContext.request.contextPath}/ModificarEstadoServlet" method="POST">
                <label for="nombrePista">Nombre de la Pista:</label>
                <select name="nombrePista" required>
                    <% 
                        if (listaPistas != null && !listaPistas.isEmpty()) {
                            for (PistaDTO pista : listaPistas) {
                    %>
                        <option value="<%= pista.getNombre() %>">
                            <%= pista.getNombre() %>
                        </option>
                    <% 
                            }
                        } else {
                    %>
                        <option disabled>No hay pistas disponibles</option>
                    <% } %>
                </select>

                <label for="estadoPista">Nuevo Estado:</label>
                <select name="estadoPista" required>
                    <option value="DISPONIBLE">Disponible</option>
                    <option value="NO_DISPONIBLE">No Disponible</option>
                </select>

                <input type="hidden" name="tipoObjeto" value="pista">
                <button type="submit">Modificar Estado de Pista</button>
            </form>
        </section>
    </div>
</body>
</html>
