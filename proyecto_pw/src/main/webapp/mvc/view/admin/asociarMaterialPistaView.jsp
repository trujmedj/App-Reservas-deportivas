<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="ProyectoPW.src.dto.PistaDTO" %>
<%@ page import="ProyectoPW.src.dto.MaterialDTO" %>

<html>
<head>
    <title>Asociar Material a Pista</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/asociarMaterialPista.css">
</head>
<body>
    <div class="container">
        <h1>Asociar Material a Pista</h1>

        <!-- Mostrar mensajes de error, éxito o información -->
        <div class="messages">
            <% 
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");

                if (error != null) { 
            %>
                <div class="alert error">
                    <%= error %>
                </div>
            <% } %>
            <% if (success != null) { %>
                <div class="alert success">
                    <%= success %>
                </div>
            <% } %>
        </div>

        <!-- Formulario para asociar material a la pista -->
        <form action="${pageContext.request.contextPath}/AsociarMaterialServlet" method="POST">
            <label for="nombrePista">Nombre de la Pista:</label>
            <select id="nombrePista" name="nombrePista" required>
                <%
                    List<PistaDTO> pistas = (List<PistaDTO>) request.getAttribute("pistas");
                    if (pistas != null && !pistas.isEmpty()) {
                        for (PistaDTO pista : pistas) {
                            out.println("<option value=\"" + pista.getNombre() + "\">" + pista.getNombre() + "</option>");
                        }
                    } else {
                        out.println("<option>No hay pistas disponibles</option>");
                    }
                %>
            </select>

            <label for="idMaterial">ID del Material:</label>
            <select id="idMaterial" name="idMaterial" required>
                <%
                    // Obtener la lista de materiales del atributo request
                 List<MaterialDTO> materiales = (List<MaterialDTO>) request.getAttribute("materiales");
                 for (MaterialDTO material : materiales) {
                     // Mostrar el ID y el Tipo del material en el desplegable
                        out.println("<option value=\"" + material.getIdMaterial() + "\">" + material.getIdMaterial() + " - " + material.getTipo() + "</option>");
                    }
%>

            </select>

            <button type="submit">Asociar Material</button>
            <a href="<%= request.getContextPath() %>/mvc/view/admin/mainAdminView.jsp">Volver</a>
        </form>
    </div>
</body>
</html>
