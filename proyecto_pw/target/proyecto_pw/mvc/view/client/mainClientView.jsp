<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat, java.util.Date" %>
<%@ page import="ProyectoPW.src.dto.JugadorDTO, java.util.Calendar" %>
<%@ page import="ProyectoPW.src.dao.ReservaDAO" %>
<%@ page import="ProyectoPW.src.dto.ReservaDTO" %>

<jsp:useBean id="usuarioBean" scope="session" class="ProyectoPW.src.display.UsuarioBean"></jsp:useBean>

<%
    // Obtener la información del jugador desde el UsuarioBean
    JugadorDTO jugador = usuarioBean.getJugador();

    // Formatear la fecha actual
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String fechaActual = sdf.format(new Date());

    // Inicializar valores predeterminados para evitar NullPointerException
    String fechaInscripcion = "No disponible";
    String fechaProximaReserva = "No hay próxima reserva";

    if (jugador != null) {
        // Obtener la fecha de inscripción del jugador
        if (jugador.getFechaInscripcion() != null) {
            fechaInscripcion = sdf.format(jugador.getFechaInscripcion());
        }

        // Obtener la próxima reserva, si existe
        ReservaDAO reservaDAO = new ReservaDAO();
        ReservaDTO reservaDTO = reservaDAO.obtenerProximaReserva(jugador.getCorreo());

        if (reservaDTO != null && reservaDTO.getFechaHora() != null) {
            fechaProximaReserva = sdf.format(reservaDTO.getFechaHora());
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Principal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mainClientView.css"> <!-- Enlace a tu archivo CSS si tienes uno -->
</head>
<body>
    <div class="container">
    
        <!-- Acciones comunes -->
        <div class="common-actions extra-links">
            <a href="<%= request.getContextPath() %>/mvc/controller/logoutController.jsp" class="back-link">Desconectar</a>
            <a href="<%= request.getContextPath() %>/mvc/controller/modificarDatosController.jsp" class="register-link">Modificar Datos</a>
        </div>

        <h1>¡Bienvenido de vuelta, <%= jugador != null ? jugador.getNombre() : "Usuario" %>!</h1>
        <p><strong>Correo:</strong> <%= jugador != null ? jugador.getCorreo() : "No disponible" %></p>
        <p><strong>Hoy es:</strong> <%= fechaActual %></p>
        <p><strong>Fecha de Inscripción:</strong> <%= fechaInscripcion %></p>
        <p><strong>¡Recuerde! Su próxima Reserva es:</strong> <%= fechaProximaReserva %></p>

        <!-- Botones dentro de la caja -->
        <div class="buttons">
            <a href="<%= request.getContextPath() %>/mvc/view/client/consultarReservasView.jsp">
                <button>Consultar Reservas</button>
            </a>
            <a href="<%= request.getContextPath() %>/mvc/view/client/buscarPistaView.jsp">
                <button>Buscar Pistas Disponibles</button>
            </a>
            <a href="<%= request.getContextPath() %>/RealizarReservaServlet">
                <button>Hacer Reserva</button>
            </a>
            <a href="<%= request.getContextPath() %>/RealizarReservaBonoServlet">
                <button>Hacer Reserva con Bono</button>
            </a>
            <a href="<%= request.getContextPath() %>/mvc/view/client/adquirirBonoView.jsp">
                <button>Adquirir Bono de Reservas</button>
            </a>
            <a href="<%= request.getContextPath() %>/ModificarReservaServlet">
                <button>Modificar Reserva</button>
            </a>
            <a href="<%= request.getContextPath() %>/CancelarReservaServlet">
                <button>Cancelar Reserva</button>
            </a>
        </div>
    </div>
</body>

</html>
