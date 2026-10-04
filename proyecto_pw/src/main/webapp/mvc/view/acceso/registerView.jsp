<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro de Usuario</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registerView.css">
</head>
<body>
    <div class="container">
        <h1>Registro de Usuario</h1>
        <p><%= request.getParameter("message") != null ? request.getParameter("message") : "" %></p>

        <form method="post" action="<%= request.getContextPath() %>/mvc/controller/registerController.jsp">
            <label for="name">Nombre:</label>
            <input type="text" name="name" required><br/>

            <label for="surname">Apellidos:</label>
            <input type="text" name="surname" required><br/>

            <label for="birthdate">Fecha de Nacimiento:</label>
            <input type="date" name="birthdate" required><br/>

            <label for="email">Email:</label>
            <input type="email" name="email" required><br/>

            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required><br/>

            <!-- Checkbox para mostrar/ocultar contraseña -->
            <label for="showPassword">
                <input type="checkbox" id="showPassword" onclick="togglePasswordVisibility()"> Mostrar Contraseña
            </label><br/>

            <label for="role">Rol:</label>
            <select name="role">
                <option value="jugador">Jugador</option>
                <option value="admin">Administrador</option>
            </select><br/>

            <input type="submit" value="Registrar">
        </form>
        
        
        <a href="<%= request.getContextPath() %>/mvc/view/loginView.jsp" class="button">¿Ya tienes cuenta? Inicia Sesión</a>
        <a href="<%= request.getContextPath() %>/index.jsp">Volver</a>
    </div>

    <!-- Enlace al script JavaScript -->
    <script src="${pageContext.request.contextPath}/js/mostrarContrasena.js"></script>
</body>
</html>
