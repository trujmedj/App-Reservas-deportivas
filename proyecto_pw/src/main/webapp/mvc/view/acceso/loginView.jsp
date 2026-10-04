<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio de Sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loginView.css">
</head>
<body>
    
    <main>
        <div class="login-container">
            <h2>Inicio de Sesión</h2>
            
            <!-- Mensaje de error o éxito -->
            <p class="message 
               <%= request.getParameter("message") != null && request.getParameter("message").contains("incorrectos") ? "error-message" : "success-message" %>">
                <%= request.getParameter("message") != null ? request.getParameter("message") : "" %>
            </p>
            
            <form method="post" action="<%= request.getContextPath() %>/mvc/controller/loginController.jsp" class="login-form">
                <div class="form-group">
                    <label for="email">Correo electrónico:</label>
                    <input type="email" id="email" name="email" required>
                </div>
                <div class="form-group">
                    <label for="password">Contraseña:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <!-- Checkbox para mostrar/ocultar contraseña -->
                <div class="form-group checkbox-group">
                    <input type="checkbox" id="showPassword" onclick="togglePasswordVisibility()">
                    <label for="showPassword">Mostrar contraseña</label>
                </div>
                <button type="submit" class="btn">Iniciar Sesión</button>
            </form>
            
            <div class="extra-links">
                <a href="<%= request.getContextPath() %>/index.jsp" class="back-link">Volver</a>
                <a href="<%= request.getContextPath() %>/mvc/view/acceso/registerView.jsp" class="register-link">¿No tienes cuenta? Regístrate</a>
            </div>
        </div>
    </main>

    <!-- Enlace al archivo externo del script -->
    <script src="${pageContext.request.contextPath}/js/mostrarContrasena.js"></script>
    
</body>
</html>
