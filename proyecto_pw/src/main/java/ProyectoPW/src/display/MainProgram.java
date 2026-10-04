/*package ProyectoPW.src.display;


import java.util.Scanner;
import ProyectoPW.src.dto.*;
import ProyectoPW.src.manager.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainProgram {

    private static GestorUsuarios gestorUsuarios = new GestorUsuarios();
    private static GestorPistas gestorPistas = new GestorPistas();
    private static GestorReservas gestorReservas = new GestorReservas();
    private static Scanner scanner = new Scanner(System.in);
    //private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("---- Menú Principal ----");
            System.out.println("1. Gestor de Usuarios");
            System.out.println("2. Gestor de Pistas");
            System.out.println("3. Gestor de Reservas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea

            switch (choice) {
                case 1:
                    menuUsuarios();
                    break;
                case 2:
                    menuPistas();
                    break;
                case 3:
                    menuReservas();
                    break;
                case 0:
                    exit = true;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    private static void menuUsuarios() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n---- Menú Gestor de Usuarios ----");
            System.out.println("1. Listar Usuarios");
            System.out.println("2. Buscar Usuario por Correo");
            System.out.println("3. Agregar Usuario");
            System.out.println("4. Modificar Usuario");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    gestorUsuarios.listarUsuarios();
                    break;
                case 2:
                    System.out.print("Ingrese el correo del usuario a buscar: ");
                    String correo = scanner.nextLine();
                    JugadorDTO usuario = gestorUsuarios.buscarUsuarioPorCorreo(correo);
                    System.out.println(usuario != null ? usuario : "Usuario no encontrado.");
                    break;
                case 3:
                    agregarUsuario();
                    break;
                case 4:
                    modificarUsuario();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    private static void agregarUsuario() {
        
        String nombre;
        while (true) {
            System.out.print("Nombre: ");
            nombre = scanner.nextLine();
            if (nombre.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s]+")) {
                break;
            } else {
                System.out.println("Error: Nombre no válido.");
            }
        }
        
        String apellido;
        while (true) {
            System.out.print("Apellido: ");
            apellido = scanner.nextLine();
            if (apellido.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s]+")) {
                break;
            } else {
                System.out.println("Error: Apellido no válido.");
            }
        }
        
        String correo;
        while (true) {
            System.out.print("Correo: ");
            correo = scanner.nextLine();
            if (correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                break;
            } else {
                System.out.println("Error: Introduzca un correo válido (ejemplo@dominio.com).");
            }
        }
        
        String fechaNacimientoStr;
        while (true) {
            System.out.print("Fecha de Nacimiento (dd/MM/yyyy): ");
            fechaNacimientoStr = scanner.nextLine();
            if (isValidDate(fechaNacimientoStr, "dd/MM/yyyy")) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    sdf.setLenient(false); // Validación estricta de la fecha
                    Date fechaNacimiento = sdf.parse(fechaNacimientoStr);

                    // Calcular si tiene al menos 18 años
                    Date fechaActual = new Date();
                    long diferenciaMilisegundos = fechaActual.getTime() - fechaNacimiento.getTime();
                    long años = (diferenciaMilisegundos / (1000L * 60 * 60 * 24 * 365)); // Convertir ms a años

                    if (años >= 18) { // Validar que tenga 18 años o más
                        break;
                    } else {
                        System.out.println("Error: Debe tener al menos 18 años.");
                    }
                } catch (ParseException e) {
                    System.out.println("Error: No se pudo analizar la fecha.");
                }
            } else {
                System.out.println("Error: Introduzca una fecha válida en el formato dd/MM/yyyy.");
            }
        }

        
     // Obtener la fecha de inscripción como la fecha actual del sistema
        Date fechaInscripcion = new Date(); // Fecha actual

        try {
            Date fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(fechaNacimientoStr);
            
            JugadorDTO jugador = new JugadorDTO(nombre, apellido, fechaNacimiento, fechaInscripcion, correo);
            gestorUsuarios.agregarJugador(jugador);
        } catch (ParseException e) {
            System.out.println("Error al parsear la fecha.");
        }
    }

    private static void modificarUsuario() {
    	
        String correoAntiguo;
        while (true) {
            System.out.print("Ingrese el correo del usuario a modificar: ");
            correoAntiguo = scanner.nextLine();

            System.out.print("Correo Introducido: " + correoAntiguo);
            
            if (correoAntiguo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                break;
            } else {
                System.out.println("Error: Introduzca un correo válido (ejemplo@dominio.com).");
            }
        }

        System.out.print("Correo Añadido: " + correoAntiguo);
        
        String nuevoNombre;
        while (true) {
            System.out.print("Nuevo Nombre: ");
            nuevoNombre = scanner.nextLine();
            if (nuevoNombre.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s]+")) {
                break;
            } else {
                System.out.println("Error: Nombre no válido.");
            }
        }
        
        String nuevoApellido;
        while (true) {
            System.out.print("Nuevo Apellido: ");
            nuevoApellido = scanner.nextLine();
            if (nuevoApellido.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s]+")) {
                break;
            } else {
                System.out.println("Error: Apellido no válido.");
            }
        }
        
        String nuevoCorreo;
        while (true) {
            System.out.print("Nuevo Correo: ");
            nuevoCorreo = scanner.nextLine();
            if (nuevoCorreo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                break;
            } else {
                System.out.println("Error: Introduzca un correo válido (ejemplo@dominio.com).");
            }
        }
        
        String nuevaFechaNacimientoStr;
        while (true) {
            System.out.print("Nueva Fecha de Nacimiento (dd/MM/yyyy): ");
            nuevaFechaNacimientoStr = scanner.nextLine();
            if (isValidDate(nuevaFechaNacimientoStr, "dd/MM/yyyy")) {
                try{
                    Date nuevaFechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(nuevaFechaNacimientoStr);
                    if (nuevaFechaNacimiento.before(new Date())) { // Validar que no sea posterior a la fecha actual
                        break;
                    } else {
                        System.out.println("Error: La fecha de nacimiento no puede ser mayor que la fecha actual.");
                    }
                } catch (ParseException e) {
                    System.out.println("Error: No se pudo analizar la fecha.");
                }
            } else {
                System.out.println("Error: Introduzca una fecha válida en el formato dd/MM/yyyy.");
            }
        }
        
        
        try {
            Date nuevaFechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(nuevaFechaNacimientoStr);
            gestorUsuarios.modificarUsuario(nuevoNombre, nuevoApellido, nuevaFechaNacimiento, nuevoCorreo, correoAntiguo);
        } catch (ParseException e) {
            System.out.println("Error al parsear la fecha.");
        }
    }

    private static void menuPistas() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n---- Menú Gestor de Pistas ----");
            System.out.println("1. Listar Pistas No Disponibles");
            System.out.println("2. Crear Pista");
            System.out.println("3. Crear Material");
            System.out.println("4. Asociar Material a Pista");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    gestorPistas.listarPistasNoDisponibles();
                    break;
                case 2:
                    crearPista(gestorPistas, scanner);
                    break;
                case 3:
                    crearMaterial(gestorPistas, scanner);
                    break;
                case 4:
                    asociarMaterialAPista();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }
    private static void crearPista(GestorPistas gestorPistas, Scanner scanner) {
       // Solicitar al usuario que ingrese los detalles de la pista
        System.out.print("Ingrese el nombre de la pista: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese el tamaño de la pista (MINIBASKET, ADULTOS, TRES_VS_TRES): ");
        String tamanoStr = scanner.nextLine().toUpperCase();
        PistaDTO.TamanoPista tamano;

        // Manejar el caso si el tamaño no es válido
        try {
            tamano = PistaDTO.TamanoPista.valueOf(tamanoStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Tamaño no válido. Debe ser MINIBASKET, ADULTOS o TRES_VS_TRES.");
            return; // Salir del método si el tamaño no es válido
        }

        System.out.print("Ingrese el número máximo de jugadores: ");
        int maxJugadores;
        try {
            maxJugadores = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Número de jugadores no válido. Debe ser un número entero.");
            return; // Salir del método si el número no es válido
        }

        System.out.print("¿La pista está disponible? (true/false): ");
        boolean disponible;
        try {
            disponible = Boolean.parseBoolean(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Valor no válido para la disponibilidad exterior. Debe ser true o false.");
            return; // Salir del método si el valor no es válido
        }


        // Solicitar si la pista es exterior
        System.out.print("¿La pista es exterior? (true/false): ");
        boolean esExterior;
        try {
            esExterior = Boolean.parseBoolean(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Valor no válido para la disponibilidad exterior. Debe ser true o false.");
            return; // Salir del método si el valor no es válido
        }

        // Crear una nueva instancia de Pista
        PistaDTO nuevaPista = new PistaDTO(nombre, disponible, esExterior, tamano, maxJugadores);

        // Usar el gestor de pistas para crear la pista
        gestorPistas.crearPista(nuevaPista);
    }

    private static void crearMaterial(GestorPistas gestorPistas, Scanner scanner) {
        // Solicitar el tipo de material
        System.out.print("Ingrese el tipo de material (PELOTAS, CANASTAS, CONOS): ");
        String tipoStr = scanner.nextLine().toUpperCase();
        MaterialDTO.TipoMaterial tipo;

        // Manejar el caso si el tipo no es válido
        try {
            tipo = MaterialDTO.TipoMaterial.valueOf(tipoStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo no válido. Debe ser PELOTAS, CANASTAS o CONOS.");
            return; // Salir del método si el tipo no es válido
        }

        System.out.print("¿Es para uso exterior? (true/false): "); 
        boolean usoExterior;
        try {
            usoExterior = Boolean.parseBoolean(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Valor no válido para la disponibilidad exterior. Debe ser true o false.");
            return; // Salir del método si el valor no es válido
        }

        System.out.print("Ingrese el estado del material (DISPONIBLE, RESERVADO, MAL_ESTADO): ");
        String estadoStr = scanner.nextLine().toUpperCase();
        MaterialDTO.EstadoMaterial estado;

        // Manejar el caso si el estado no es válido
        try {
            estado = MaterialDTO.EstadoMaterial.valueOf(estadoStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Estado no válido. Debe ser DISPONIBLE, RESERVADO o MAL_ESTADO.");
            return; // Salir del método si el estado no es válido
        }

        // Generar un ID único para el material
        int idMaterial = generarIdUnicoMaterial(gestorPistas); // Método que debes implementar

        // Crear una nueva instancia de Material
        MaterialDTO nuevoMaterial = new MaterialDTO(idMaterial, tipo, usoExterior, estado);

        // Usar el gestor de pistas para crear el material
        gestorPistas.crearMaterial(nuevoMaterial);
    }

    // Método para generar un ID único (esto es solo un ejemplo)
    private static int generarIdUnicoMaterial(GestorPistas gestorPistas) {
        // Aquí deberías implementar la lógica para generar un ID único.
        return (int) (Math.random() * 1000); // Cambia esta lógica según tus necesidades
    }

    private static void asociarMaterialAPista() {
        System.out.print("Ingrese el ID del material: ");
        int idMaterial = Integer.parseInt(scanner.nextLine());
        MaterialDTO material = gestorPistas.buscarMaterialPorID(idMaterial);
    
        if (material == null) {
            System.out.println("Material no encontrado.");
            return;
        }
    
        System.out.print("Ingrese el nombre de la pista: ");
        String nombrePista = scanner.nextLine();
        PistaDTO pista = gestorPistas.buscarPistaPorNombre(nombrePista);
    
        if (pista == null) {
            System.out.println("Pista no encontrada.");
            return;
        }
    
        if (gestorPistas.asociarMaterialAPista(material, pista)) {
            System.out.println("Material asociado a la pista con éxito.");
        } else {
            System.out.println("No se pudo asociar el material a la pista.");
        }
    } 
    
    

    private static void menuReservas() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n---- Menú Gestor de Reservas ----");
            System.out.println("1. Hacer Reserva Individual");
            System.out.println("2. Hacer Reserva con Bono");
            System.out.println("3. Modificar Reserva");
            System.out.println("4. Cancelar Reserva");
            System.out.println("5. Contar Reservas Futuras");
            System.out.println("6. Listar Reservas por Fecha y Pista");
            System.out.println("7. Crear Bono");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer de entrada

            switch (choice) {
                case 1:
                    hacerReservaIndividual(gestorReservas, scanner, gestorUsuarios, gestorPistas);
                    break;
                case 2:
                    hacerReservaConBono(gestorReservas, scanner, gestorPistas, gestorUsuarios);
                    break;
                case 3:
                    modificarReserva(gestorReservas, scanner);
                    break;
                case 4:
                    cancelarReserva(gestorReservas, scanner);
                    break;
                case 5:
                    contarReservasFuturas(gestorReservas);
                    break;
                case 6:
                    listarReservasPorFechaYPista(gestorReservas, scanner, gestorPistas);
                    break;
                case 7:
                	crearBono(gestorReservas,scanner);
                	break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    // Método para hacer una reserva individual
    private static void hacerReservaIndividual(GestorReservas gestorReservas, Scanner scanner, GestorUsuarios gestorUsuarios, GestorPistas gestorPistas) {
        System.out.print("Ingrese el correo del jugador: ");
        String correo = scanner.nextLine();
        JugadorDTO jugador = gestorUsuarios.buscarUsuarioPorCorreo(correo);
        if (jugador == null) {
            System.out.println("Jugador no encontrado.");
            return;
        }
        ReservaDTO reservaBuscada = gestorReservas.encontrarReservaPorJugador(correo);
        if (reservaBuscada != null) {
        	System.out.println("Este jugador ya tiene una reserva activa.");
            return;
        }
        
        System.out.print("Ingrese la fecha y hora de la reserva (formato: dd/MM/yyyy HH:mm): ");
        String fechaHoraStr = scanner.nextLine();
        Date fechaHora;
        try {
            fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fechaHoraStr);
    
            // Obtener la fecha actual
            Date fechaActual = new Date();
    
            // Validar si la fecha ingresada es anterior a la fecha actual
            if (fechaHora.before(fechaActual)) {
                System.out.println("No puede reservar una pista para una fecha anterior a la actual.");
                return;
            }
        } catch (ParseException e) {
            System.out.println("Formato de fecha inválido. Intente de nuevo.");
            return;
        }

        System.out.print("Ingrese la duración de la reserva (60, 90 o 120 minutos): ");
        int duracion = scanner.nextInt();

        // Validar la duración ingresada
        while (duracion != 60 && duracion != 90 && duracion != 120) {
            System.out.println("Error: Duración inválida. Solo se permiten 60, 90 o 120 minutos.");
            System.out.print("Por favor, ingrese una duración válida: ");
            duracion = scanner.nextInt();
        }
        
        scanner.nextLine(); // Limpiar el buffer de entrada

        System.out.print("Ingrese el nombre de la pista: ");
        String nombrePista = scanner.nextLine();
        PistaDTO pista = gestorPistas.buscarPistaPorNombre(nombrePista); 
        if (pista == null) {
            System.out.println("Pista no encontrada.");
            return;
        }
        
        System.out.print("Ingrese el número de adultos: ");
        int numAdultos = scanner.nextInt();
        System.out.print("Ingrese el número de niños: ");
        int numNinos = scanner.nextInt();

        ReservaDTO reserva = gestorReservas.hacerReservaIndividual(jugador, fechaHora, duracion, pista, numAdultos, numNinos);
        if (reserva != null) {
            System.out.println("Reserva realizada con éxito: " + reserva);
        } else {
            System.out.println("Error al realizar la reserva.");
        }
    }

    // Método para hacer una reserva con bono
    private static void hacerReservaConBono(GestorReservas gestorReservas, Scanner scanner, GestorPistas gestorPistas, GestorUsuarios gestorUsuarios) {
        System.out.print("Ingrese el correo del jugador: ");
        String correo = scanner.nextLine();
        JugadorDTO jugador = gestorUsuarios.buscarUsuarioPorCorreo(correo);
        if (jugador == null) {
            System.out.println("Jugador no encontrado.");
            return;
        } 

        ReservaDTO reservaBuscada = gestorReservas.encontrarReservaPorJugador(correo);
        if (reservaBuscada != null) {
        	System.out.println("Este jugador ya tiene una reserva activa.");
            return;
        }

        BonoDTO bono = gestorReservas.buscarBonoPorJugador(correo);
        if (bono == null) {
            System.out.println("Bono no encontrado.");
            return;
        }

        System.out.print("Ingrese la fecha y hora de la reserva (formato: dd/MM/yyyy HH:mm): ");
        String fechaHoraStr = scanner.nextLine();
        Date fechaHora;
        try {
            fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fechaHoraStr);
    
            // Obtener la fecha actual
            Date fechaActual = new Date();
    
            // Validar si la fecha ingresada es anterior a la fecha actual
            if (fechaHora.before(fechaActual)) {
                System.out.println("No puede reservar una pista para una fecha anterior a la actual.");
                return;
            }
        } catch (ParseException e) {
            System.out.println("Formato de fecha inválido. Intente de nuevo.");
            return;
        }

        System.out.print("Ingrese la duración de la reserva (60, 90 o 120 minutos): ");
        int duracion = scanner.nextInt();

        System.out.print("Ingrese el nombre de la pista: ");
        scanner.nextLine(); // Limpiar el buffer
        String nombrePista = scanner.nextLine();
        PistaDTO pista = gestorPistas.buscarPistaPorNombre(nombrePista); 
        if (pista == null) {
            System.out.println("Pista no encontrada.");
            return;
        }

        System.out.print("Ingrese el número de adultos: ");
        int numAdultos = scanner.nextInt();
        System.out.print("Ingrese el número de niños: ");
        int numNinos = scanner.nextInt();

        ReservaDTO reserva = gestorReservas.hacerReservaBono(jugador, fechaHora, duracion, pista, bono, numAdultos, numNinos);
        if (reserva != null) {
            System.out.println("Reserva realizada con bono con éxito: " + reserva);
        } else {
            System.out.println("Error al realizar la reserva con bono.");
        }
    }

    // Método para modificar una reserva
    private static void modificarReserva(GestorReservas gestorReservas, Scanner scanner) {
        System.out.print("Ingrese el correo del jugador de la reserva a modificar: ");
        String correo = scanner.nextLine();
        ReservaDTO reserva = gestorReservas.encontrarReservaPorJugador(correo); // Buscar reserva del jugador

        if (reserva == null) {
            System.out.println("No se encontró ninguna reserva para el jugador.");
            return;
        }

       System.out.print("Ingrese la fecha y hora de la reserva (formato: dd/MM/yyyy HH:mm): ");
        String fechaHoraStr = scanner.nextLine();
        Date nuevaFechaHora;
        try {
            nuevaFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fechaHoraStr);
    
            // Obtener la fecha actual
            Date fechaActual = new Date();
    
            // Validar si la fecha ingresada es anterior a la fecha actual
            if (nuevaFechaHora.before(fechaActual)) {
                System.out.println("No puede reservar una pista para una fecha anterior a la actual.");
                return;
            }
        } catch (ParseException e) {
            System.out.println("Formato de fecha inválido. Intente de nuevo.");
            return;
        }

        System.out.print("Ingrese la nueva duración de la reserva (60, 90 o 120 minutos): ");
        int nuevaDuracion = scanner.nextInt();

        if (gestorReservas.modificarReserva(reserva, nuevaFechaHora, nuevaDuracion)) {
            System.out.println("Reserva modificada con éxito.");
        } else {
            System.out.println("No se pudo modificar la reserva.");
        }
    }

    // Método para cancelar una reserva
    private static void cancelarReserva(GestorReservas gestorReservas, Scanner scanner) {
        System.out.print("Ingrese el correo del jugador de la reserva a cancelar: ");
        String correo = scanner.nextLine();
        ReservaDTO reserva = gestorReservas.encontrarReservaPorJugador(correo); // Buscar reserva del jugador

        if (reserva == null) {
            System.out.println("No se encontró ninguna reserva para el jugador.");
            return;
        }

        if (gestorReservas.cancelarReserva(reserva)) {
            System.out.println("Reserva cancelada con éxito.");
        } else {
            System.out.println("No se pudo cancelar la reserva.");
        }
    }

    // Método para contar reservas futuras
    private static void contarReservasFuturas(GestorReservas gestorReservas) {
        int count = gestorReservas.contarReservasFuturas(); // Usar el método de la clase GestorReservas
        System.out.println("Número de reservas futuras: " + count);
    }

    // Método para listar reservas por fecha y pista
    private static void listarReservasPorFechaYPista(GestorReservas gestorReservas, Scanner scanner, GestorPistas gestorPistas) {
        System.out.print("Ingrese la fecha y hora de la reserva (formato: dd/MM/yyyy): ");
        String fechaStr = scanner.nextLine();
        Date fecha;
        try {
            fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
    
            // Obtener la fecha actual
            Date fechaActual = new Date();
    
            // Validar si la fecha ingresada es anterior a la fecha actual
            if (fecha.before(fechaActual)) {
                System.out.println("No puede reservar una pista para una fecha anterior a la actual.");
                return;
            }
        } catch (ParseException e) {
            System.out.println("Formato de fecha inválido. Intente de nuevo.");
            return;
        }

        System.out.println("Ingrese el nombre de la pista: ");
        String nombrePista = scanner.nextLine();
        PistaDTO pista = gestorPistas.buscarPistaPorNombre(nombrePista); 
        if (pista == null) {
            System.out.println("Pista no encontrada.");
            return;
        }


        List<ReservaDTO> reservasEncontradas = gestorReservas.listarReservasPorFechaYPista(fecha, pista);
        if (reservasEncontradas.isEmpty()) {
            System.out.println("No se encontraron reservas para la fecha y pista especificadas.");
        } else {
            System.out.println("Reservas encontradas:");
            for (ReservaDTO r : reservasEncontradas) {
                System.out.println(r);
            }
        }

    }

    
    public static void crearBono(GestorReservas gestorReservas, Scanner scanner) {

        // Solicitar el correo del jugador
        System.out.print("Ingrese el correo del jugador: ");
        String correoJugador = scanner.nextLine();
        
        BonoDTO bonoBuscado = gestorReservas.buscarBonoPorJugador(correoJugador);
        if (bonoBuscado != null) {
        	System.out.println("Este jugador ya tiene un bono");
            return;
        }


        // Solicitar el tipo de reserva
        System.out.println("Seleccione el tipo de reserva:");
        System.out.println("1. Infantil");
        System.out.println("2. Familiar");
        System.out.println("3. Adultos");
        int tipoReserva = scanner.nextInt();

        // Crear el bono con los datos proporcionados
        BonoDTO bono = gestorReservas.crearBono(correoJugador, tipoReserva);

        if (bono != null) {
            System.out.println("¡Bono creado exitosamente!");
            System.out.println(bono.toString());
        } else {
            System.out.println("Error al crear el bono. Verifique los datos e intente de nuevo.");
        }
    }*/

    
    //------------------------------FUNCIONES EXTRA-----------------------------------
    
    /*
     * Verifica si una fecha es válida según un formato específico.
     *
     * @param dateStr   La fecha como cadena.
     * @param formatStr El formato esperado (por ejemplo, "dd/MM/yyyy").
     * @return true si la fecha es válida, false en caso contrario.
     *//* 
    private static boolean isValidDate(String dateStr, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    

}*/