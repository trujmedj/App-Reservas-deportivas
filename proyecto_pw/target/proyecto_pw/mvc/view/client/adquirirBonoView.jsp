<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Adquirir Bono</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adquirirBonoView.css">
</head>
<body>
    <div class="container">
        <h1>Adquirir Bono</h1>
        <form action="<%= request.getContextPath() %>/AdquirirBonoServlet" method="post">
            <label for="tipoReserva">Seleccione el tipo de reserva:</label>
            <select name="tipoReserva" id="tipoReserva" required>
                <option value="1">Infantil</option>
                <option value="2">Familiar</option>
                <option value="3">Adultos</option>
            </select>
            <button type="submit">Adquirir Bono</button>
        </form>
        <a href="<%= request.getContextPath() %>/mvc/view/client/mainClientView.jsp">Volver</a>

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
    </div>
</body>
</html>
