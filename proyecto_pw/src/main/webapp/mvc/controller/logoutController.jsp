<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Invalidar la sesión
    if (session != null) {
        session.invalidate();
    }

    // Redirigir a la página de inicio de sesión
    response.sendRedirect(request.getContextPath() + "/index.jsp");
%>
