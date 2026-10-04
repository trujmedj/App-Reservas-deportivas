<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat, java.util.List" %>
<%@ page import="ProyectoPW.src.dao.JugadorDAO, ProyectoPW.src.dto.JugadorDTO" %>
<%@ page import="ProyectoPW.src.dao.ReservaDAO" %>

<%
    // Crear los DAO para acceder a los datos
    JugadorDAO jugadorDAO = new JugadorDAO();
    ReservaDAO reservaDAO = new ReservaDAO();

    // Obtener la lista de clientes
    List<JugadorDTO> listaJugadores = jugadorDAO.listarJugadores();

    // Formateador de fechas
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vista Administrador</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mainAdminView.css">
</head>
<body>
    <div class="container">
        <h1>Listado de Clientes</h1>
        
        <!-- Acciones comunes -->
        <div class="common-actions extra-links">
            <a href="<%= request.getContextPath() %>/mvc/controller/logoutController.jsp" class="back-link">Desconectar</a>
            <a href="<%= request.getContextPath() %>/mvc/controller/modificarDatosController.jsp" class="register-link">Modificar Datos</a>
        </div>
        
        <table border="1" class="table">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Fecha de Inscripción</th>
                    <th>Reservas Completadas</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (listaJugadores != null && !listaJugadores.isEmpty()) {
                        for (JugadorDTO jugador : listaJugadores) {
                            // Validar y formatear la fecha de inscripción
                            String fechaInscripcion = (jugador.getFechaInscripcion() != null)
                                    ? sdf.format(jugador.getFechaInscripcion())
                                    : "No disponible";

                            // Obtener el número de reservas completadas
                            int reservasCompletadas = reservaDAO.contarReservasCompletadas(jugador.getCorreo());

                            // Renderizar la fila de la tabla
                            %>
                            <tr>
                                <td><%= jugador.getNombre() %></td>
                                <td><%= fechaInscripcion %></td>
                                <td><%= reservasCompletadas %></td>
                            </tr>
                            <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="3">No hay clientes registrados</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

        <!-- Botones para redirigir a las páginas de gestión -->
        <h2>Gestión de Material y Pistas</h2>
        <div class="buttons">
            <a href="<%= request.getContextPath() %>/mvc/view/admin/altaMaterialView.jsp">
                <button>Dar de Alta Material</button>
            </a>
            <a href="<%= request.getContextPath() %>/mvc/view/admin/altaPistaView.jsp">
                <button>Dar de Alta Pista</button>
            </a>
            <a href="<%= request.getContextPath() %>/AsociarMaterialServlet">
                <button>Asociar Materiales a Pista</button>
            </a>
            <a href="<%= request.getContextPath() %>/mvc/view/admin/modificarEstadoView.jsp">
                <button>Modificar Estado Pistas/Materiales</button>
            </a>
            <a href="<%= request.getContextPath() %>/mvc/view/admin/eliminarReservasFuturasView.jsp">
                <button>Eliminar Reservas Futuras</button>
            </a>
        </div>
    </div>
</body>
</html>
