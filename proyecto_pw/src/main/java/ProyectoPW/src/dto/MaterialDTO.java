package ProyectoPW.src.dto;

/**
 * Clase que representa un material utilizado en actividades, con atributos
 * que indican su tipo, estado y características de uso.
 */
public class MaterialDTO {

    /**
     * Enumeración que define los tipos de material disponibles.
     */
    public enum TipoMaterial {
        PELOTAS,   // Tipo de material: Pelotas
        CANASTAS,  // Tipo de material: Canastas
        CONOS      // Tipo de material: Conos
    }

    /**
     * Enumeración que define los estados posibles del material.
     */
    public enum EstadoMaterial {
        DISPONIBLE,  // Estado del material: Disponible para uso
        RESERVADO,   // Estado del material: Reservado
        MAL_ESTADO   // Estado del material: En mal estado
    }

    // Atributos del material
    private int idMaterial_;        // Identificador único del material
    private TipoMaterial tipo_;      // Tipo de material
    private boolean usoExterior_;    // Indica si el material es para uso exterior
    private EstadoMaterial estado_;   // Estado actual del material

    /**
     * Constructor vacío que inicializa un objeto Material.
     */
    public MaterialDTO() {
    }

    /**
     * Constructor parametrizado para inicializar un objeto Material
     * con los datos proporcionados.
     *
     * @param idMaterial   Identificador del material
     * @param tipo        Tipo de material (PELOTAS, CANASTAS, CONOS)
     * @param usoExterior Indica si el material es para uso exterior
     * @param estado      Estado del material (DISPONIBLE, RESERVADO, MAL_ESTADO)
     */
    public MaterialDTO(int idMaterial, TipoMaterial tipo, boolean usoExterior, EstadoMaterial estado) {
        idMaterial_ = idMaterial;
        tipo_ = tipo;
        usoExterior_ = usoExterior;
        estado_ = estado;
    }

    /**
     * Obtiene el identificador del material.
     *
     * @return el identificador del material
     */
    public int getIdMaterial() {
        return idMaterial_;
    }

    /**
     * Establece el identificador del material.
     *
     * @param idMaterial el nuevo identificador del material
     */
    public void setIdMaterial(int idMaterial) {
        idMaterial_ = idMaterial;
    }

    /**
     * Obtiene el tipo de material.
     *
     * @return el tipo de material
     */
    public TipoMaterial getTipo() {
        return tipo_;
    }

    /**
     * Establece el tipo de material.
     *
     * @param tipo el nuevo tipo de material
     */
    public void setTipo(TipoMaterial tipo) {
        tipo_ = tipo;
    }

    /**
     * Indica si el material es para uso exterior.
     *
     * @return true si el material es para uso exterior, false si es para uso interior
     */
    public boolean isUsoExterior() {
        return usoExterior_;
    }

    /**
     * Establece si el material es para uso exterior.
     *
     * @param usoExterior true si el material es para uso exterior, false para uso interior
     */
    public void setUsoExterior(boolean usoExterior) {
        usoExterior_ = usoExterior;
    }

    /**
     * Obtiene el estado del material.
     *
     * @return el estado del material
     */
    public EstadoMaterial getEstado() {
        return estado_;
    }

    /**
     * Establece el estado del material.
     *
     * @param estado el nuevo estado del material
     */
    public void setEstado(EstadoMaterial estado) {
        estado_ = estado;
    }

    /**
     * Devuelve una representación en String de la información del material.
     *
     * @return la información del material en formato String
     */
    @Override
    public String toString() {
        return "Material{" +
                "IDMATERIAL: " + idMaterial_ +
                ", TIPO: " + tipo_ +
                ", USO: " + (usoExterior_ ? "Exterior" : "Interior") +
                ", ESTADO: " + estado_ +
                '}';
    }
}
