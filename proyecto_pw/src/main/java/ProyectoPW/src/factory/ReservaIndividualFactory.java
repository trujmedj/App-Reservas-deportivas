package ProyectoPW.src.factory;

import ProyectoPW.src.dto.PistaDTO;
import ProyectoPW.src.dto.JugadorDTO;
import ProyectoPW.src.dto.ReservaAdultosDTO;
import ProyectoPW.src.dto.ReservaDTO;
import ProyectoPW.src.dto.ReservaFamiliarDTO;
import ProyectoPW.src.dto.ReservaInfantilDTO;
import java.util.Date;

/**
 * Clase responsable de la creación de reservas individuales sin bono.
 * Esta clase proporciona un método para crear reservas individuales, 
 * con validación de los parámetros y asignación del precio según la duración de la reserva.
 */
public class ReservaIndividualFactory {

    /**
     * Crea una reserva individual según el número de adultos y niños.
     *
     * Este método realiza una serie de validaciones para asegurarse de que 
     * la reserva es posible. Se verifica la duración, la disponibilidad de la pista, 
     * la validez del número de jugadores y la antigüedad del jugador para aplicar un descuento.
     *
     * @param jugador      El jugador que realiza la reserva.
     * @param fechaHora    La fecha y hora de la reserva.
     * @param duracion     La duración de la reserva en minutos.
     * @param pista        La pista en la que se realiza la reserva.
     * @param numAdultos   El número de adultos (para reservas familiares o de adultos).
     * @param numNinos     El número de niños (para reservas infantiles o familiares).
     * @return La reserva creada si todos los parámetros son válidos, o null si no se puede realizar la reserva.
     */
    public static ReservaDTO crearReserva(JugadorDTO jugador, Date fechaHora, int duracion, PistaDTO pista, int numAdultos, int numNinos) {
        // Precio según la duración de la reserva
        float precio = 0;
        switch (duracion) {
            case 60:
                precio = 20f; // Precio para 60 minutos
                break;
            case 90:
                precio = 30f; // Precio para 90 minutos
                break;
            case 120:
                precio = 40f; // Precio para 120 minutos
                break;
            default:
                System.out.println("Duración no válida. Las opciones disponibles son 60, 90 o 120 minutos.");
                return null; // Retorna null si la duración no es válida
        }

        // Verificar si el jugador tiene más de 2 años de antigüedad para el descuento
        float descuento = 0;
        if (esAntiguo(jugador)) { // Llamada a esAntiguo() para verificar la antigüedad del jugador
            descuento = precio * 0.1f; // 10% de descuento
        }

        // Aplicar el descuento al precio
        precio -= descuento;

        // Comprobar que la pista esté disponible
        if (!pista.isDisponible()) {
            System.out.println("La pista seleccionada no está disponible.");
            return null; // Retorna null si la pista no está disponible
        }

        // Verificar que la suma de numAdultos y numNinos no exceda el máximo de jugadores de la pista
        int maximoJugadores = pista.getMaxJugadores(); // Obtener el máximo de jugadores permitidos en la pista
        if (numAdultos + numNinos > maximoJugadores) {
            System.out.println("El número total de jugadores excede el máximo permitido en la pista.");
            return null; // Retorna null si el número total de jugadores excede el límite
        }

        // Determinar el tipo de reserva según el número de adultos y niños
        ReservaDTO reserva;
        boolean isBono = false; // Indicador de que no es una reserva con bono
        if (numAdultos > 0 && numNinos == 0) {
            // Reserva de adultos
            if (!pista.getTamano().equals(PistaDTO.TamanoPista.ADULTOS)) {
                System.out.println("La reserva de adultos solo se puede hacer en pistas de tamaño ADULTOS.");
                return null; // Retorna null si la pista no es adecuada para adultos
            }
            reserva = new ReservaAdultosDTO(jugador.getCorreo(), fechaHora, duracion, pista.getNombre(), precio, descuento, numAdultos, isBono);
        } else if (numAdultos == 0 && numNinos > 0) {
            // Reserva infantil
            if (!pista.getTamano().equals(PistaDTO.TamanoPista.MINIBASKET)) {
                System.out.println("La reserva infantil solo se puede hacer en pistas de tamaño MINIBASKET.");
                return null; // Retorna null si la pista no es adecuada para niños
            }
            reserva = new ReservaInfantilDTO(jugador.getCorreo(), fechaHora, duracion, pista.getNombre(), precio, descuento, numNinos, isBono);
        } else if (numAdultos > 0 && numNinos > 0) {
            // Reserva familiar
            if (!pista.getTamano().equals(PistaDTO.TamanoPista.MINIBASKET) && !pista.getTamano().equals(PistaDTO.TamanoPista.TRES_VS_TRES)) {
                System.out.println("La reserva familiar solo se puede hacer en pistas de tamaño MINIBASKET o TRES_VS_TRES.");
                return null; // Retorna null si la pista no es adecuada para reservas familiares
            }
            reserva = new ReservaFamiliarDTO(jugador.getCorreo(), fechaHora, duracion, pista.getNombre(), precio, descuento, numAdultos, numNinos, isBono);
        } else {
            // Si no hay adultos ni niños
            throw new IllegalArgumentException("Debe haber al menos un adulto o un niño para realizar la reserva.");
        }

        return reserva; // Retorna la reserva creada
    }

    /**
     * Verifica si el jugador tiene más de 2 años de antigüedad.
     *
     * Este método determina si el jugador es considerado "antiguo" 
     * (si tiene más de 2 años de antigüedad) y, en ese caso, aplica un descuento.
     *
     * @param jugador El jugador a verificar.
     * @return true si el jugador tiene más de 2 años de antigüedad, false en caso contrario.
     */
    private static boolean esAntiguo(JugadorDTO jugador) {
        return jugador.calcularAntiguedad() > 2; // Considera antiguo si la antigüedad es mayor a 2 años
    }
}
