package ProyectoPW.src.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una pista de deportes, con atributos que indican su 
 * nombre, disponibilidad, tamaño, número máximo de jugadores y los materiales 
 * asociados.
 */
public class PistaDTO {

    /**
     * Enumeración para el tamaño de la pista.
     */
    public enum TamanoPista {
        MINIBASKET,  // Tamaño para minibasket
        ADULTOS,     // Tamaño para adultos
        TRES_VS_TRES  // Tamaño para partidos de tres contra tres
    }

    // Variables privadas
    private String nombre_;         // Nombre de la pista
    private boolean disponible_;     // Indica si la pista está disponible
    private boolean esExterior_;     // Indica si la pista es exterior
    private TamanoPista tamano_;    // Tamaño de la pista
    private int maxJugadores_;       // Número máximo de jugadores en la pista
    private List<MaterialDTO> materiales_; // Lista de materiales asociados a la pista

    /**
     * Constructor vacío que inicializa una pista sin parámetros.
     * Se inicializa la lista de materiales.
     */
    public PistaDTO() {
        materiales_ = new ArrayList<>();
    }

    /**
     * Constructor parametrizado para inicializar una pista con los datos 
     * proporcionados.
     *
     * @param nombre       Nombre de la pista
     * @param disponible   Indica si la pista está disponible
     * @param esExterior   Indica si la pista es exterior
     * @param tamano      Tamaño de la pista
     * @param maxJugadores Número máximo de jugadores en la pista
     */
    public PistaDTO(String nombre, boolean disponible, boolean esExterior, TamanoPista tamano, int maxJugadores) {
        nombre_ = nombre;
        disponible_ = disponible;
        esExterior_ = esExterior;
        tamano_ = tamano;
        maxJugadores_ = maxJugadores;
        materiales_ = new ArrayList<>();
    }

    // Getters y Setters

    /**
     * Obtiene el nombre de la pista.
     *
     * @return el nombre de la pista
     */
    public String getNombre() {
        return nombre_;
    }

    /**
     * Establece el nombre de la pista.
     *
     * @param nombre el nuevo nombre de la pista
     */
    public void setNombre(String nombre) {
        nombre_ = nombre;
    }

    /**
     * Indica si la pista está disponible.
     *
     * @return true si la pista está disponible, false en caso contrario
     */
    public boolean isDisponible() {
        return disponible_;
    }

    /**
     * Establece la disponibilidad de la pista.
     *
     * @param disponible true si la pista está disponible, false en caso contrario
     */
    public void setDisponible(boolean disponible) {
        disponible_ = disponible;
    }

    /**
     * Indica si la pista es exterior.
     *
     * @return true si la pista es exterior, false en caso contrario
     */
    public boolean isEsExterior() {
        return esExterior_;
    }

    /**
     * Establece si la pista es exterior.
     *
     * @param esExterior true si la pista es exterior, false si es interior
     */
    public void setEsExterior(boolean esExterior) {
        esExterior_ = esExterior;
    }

    /**
     * Obtiene el tamaño de la pista.
     *
     * @return el tamaño de la pista
     */
    public TamanoPista getTamano() {
        return tamano_;
    }

    /**
     * Establece el tamaño de la pista.
     *
     * @param tamano el nuevo tamaño de la pista
     */
    public void setTamano(TamanoPista tamano) {
        tamano_ = tamano;
    }

    /**
     * Obtiene el número máximo de jugadores en la pista.
     *
     * @return el número máximo de jugadores
     */
    public int getMaxJugadores() {
        return maxJugadores_;
    }

    /**
     * Establece el número máximo de jugadores en la pista.
     *
     * @param maxJugadores el nuevo número máximo de jugadores
     */
    public void setMaxJugadores(int maxJugadores) {
        maxJugadores_ = maxJugadores;
    }

    /**
     * Obtiene la lista de materiales asociados a la pista.
     *
     * @return la lista de materiales
     */
    public List<MaterialDTO> getMateriales() {
        return materiales_;
    }

    /**
     * Establece la lista de materiales asociados a la pista.
     *
     * @param materiales la nueva lista de materiales
     */
    public void setMateriales(List<MaterialDTO> materiales) {
        materiales_ = materiales;
    }

    /**
     * Devuelve una representación en String de la información de la pista.
     *
     * @return la información de la pista en formato String
     */
    @Override
    public String toString() {
        return "Pista{" +
                "NOMBRE: '" + nombre_ + '\'' +
                ", DISPONIBLE: " + disponible_ +
                ", ESEXTERIOR: " + esExterior_ +
                ", TAMANO: " + tamano_ +
                ", MAXJUGADORES: " + maxJugadores_ +
                ", MATERIALES: " + materiales_ +
                '}';
    }

    /**
     * Consulta los materiales disponibles en la pista.
     *
     * @return una lista de materiales que están disponibles
     */
    public List<MaterialDTO> consultarMaterialesDisponibles() {
        List<MaterialDTO> materialesDisponibles = new ArrayList<MaterialDTO>();

        for (MaterialDTO material : materiales_) {
            // Si el material está disponible, se agrega a la lista de materiales disponibles
            if (material.getEstado() == MaterialDTO.EstadoMaterial.DISPONIBLE) {
                materialesDisponibles.add(material);
            }
        }

        return materialesDisponibles;
    }

    /**
     * Asocia un material a la pista.
     *
     * @param material el material que se desea asociar a la pista
     * @return true si el material fue asociado exitosamente, false en caso contrario
     */
    public boolean asociarMaterialAPista(MaterialDTO material) {
        // Comprobar si el material está reservado
        if (material.getEstado() == MaterialDTO.EstadoMaterial.RESERVADO) {
            System.out.println("No se pueden añadir materiales que estén reservados.");
            return false;
        }

        // Comprobar si el material es de exterior en una pista exterior
        if (esExterior_ && !material.isUsoExterior()) {
            System.out.println("No se pueden añadir materiales de interior a una pista exterior.");
            return false;
        }

        // Limitar la cantidad de pelotas, canastas y conos
        long countPelotas = 0, countCanastas = 0, countConos = 0;
        for (MaterialDTO m : materiales_) {
            if (m.getTipo() == MaterialDTO.TipoMaterial.PELOTAS) countPelotas++;
            else if (m.getTipo() == MaterialDTO.TipoMaterial.CANASTAS) countCanastas++;
            else if (m.getTipo() == MaterialDTO.TipoMaterial.CONOS) countConos++;
        }

        if (material.getTipo() == MaterialDTO.TipoMaterial.PELOTAS && countPelotas >= 12) {
            System.out.println("No se pueden añadir más de 12 pelotas.");
            return false;
        }
        if (material.getTipo() == MaterialDTO.TipoMaterial.CANASTAS && countCanastas >= 2) {
            System.out.println("No se pueden añadir más de 2 canastas.");
            return false;
        }
        if (material.getTipo() == MaterialDTO.TipoMaterial.CONOS && countConos >= 20) {
            System.out.println("No se pueden añadir más de 20 conos.");
            return false;
        }

        // Si todas las comprobaciones son satisfactorias, se añade el material
        materiales_.add(material);
        return true;
    }
}
