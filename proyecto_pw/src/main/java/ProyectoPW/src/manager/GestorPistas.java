package ProyectoPW.src.manager;

import ProyectoPW.src.dto.PistaDTO;
import ProyectoPW.src.dto.MaterialDTO;
import ProyectoPW.src.dao.PistaDAO;
import ProyectoPW.src.dao.MaterialDAO;

import java.util.List;
import java.util.ArrayList;

/**
 * Clase que gestiona las operaciones relacionadas con las pistas y materiales.
 * Esta clase interactúa con las clases DAO para realizar operaciones de persistencia
 * en la base de datos y proporciona métodos para crear, asociar, listar y eliminar pistas y materiales.
 */
public class GestorPistas {

    private PistaDAO pistaDAO;         // DAO para manejar las pistas
    private MaterialDAO materialDAO;   // DAO para manejar los materiales

    /**
     * Constructor que inicializa los DAOs necesarios para la gestión de pistas y materiales.
     */
    public GestorPistas() {
        this.pistaDAO = new PistaDAO();         // Inicializamos el PistaDAO
        this.materialDAO = new MaterialDAO();   // Inicializamos el MaterialDAO
    }

    /**
     * Crea una nueva pista y la añade al sistema.
     *
     * @param pista La pista a crear y añadir al sistema.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    public boolean crearPista(PistaDTO pista) {
        return pistaDAO.insertarPista(pista);  // Llama al DAO para insertar la pista en la base de datos
    }

    /**
     * Crea un nuevo material y lo añade al sistema.
     *
     * @param material El material a crear y añadir al sistema.
     * @return El material creado si la creación fue exitosa, null en caso contrario.
     */
    public MaterialDTO crearMaterial(MaterialDTO material) {
    	return materialDAO.insertarMaterial(material);  // Llama al DAO para insertar el material en la base de datos
    }

    /**
     * Asocia un material a una pista si se cumplen las restricciones.
     *
     * Primero valida si la asociación es posible en el DTO, luego intenta realizar la operación en la base de datos.
     * Si la operación en la base de datos es exitosa, actualiza el estado del material.
     *
     * @param material El material a asociar a la pista.
     * @param pista    La pista a la que se desea asociar el material.
     * @return true si la asociación fue exitosa, false en caso contrario.
     */
    public boolean asociarMaterialAPista(MaterialDTO material, PistaDTO pista) {
        // Primero, validamos y asociamos en el DTO
        boolean asociadoEnDTO = pista.asociarMaterialAPista(material);
        
        if (asociadoEnDTO) {
            // Si la validación en el DTO pasa, intentamos asociar en la base de datos
            boolean asociadoEnBD = pistaDAO.asociarMaterialAPista(pista.getNombre(), material.getIdMaterial());
            
            if (asociadoEnBD) {
                // Si la operación en la base de datos fue exitosa, actualizamos el estado del material
                material.setEstado(MaterialDTO.EstadoMaterial.RESERVADO);
                return true;
            } else {
                // Si la base de datos falla, revertimos el cambio en el DTO
                pista.getMateriales().remove(material);
                return false;
            }
        } else {
            // Si las restricciones en el DTO fallan, no intentamos en la base de datos
            return false;
        }
    }

    /**
     * Lista todas las pistas que no están disponibles para uso.
     *
     * @return Una lista de pistas no disponibles.
     */
    public List<PistaDTO> listarPistasNoDisponibles() {
        // Obtener todas las pistas
        List<PistaDTO> todasLasPistas = pistaDAO.obtenerTodasLasPistas();

        // Crear una lista para las pistas no disponibles
        List<PistaDTO> noDisponibles = new ArrayList<PistaDTO>();

        // Filtrar las pistas que no están disponibles
        for (PistaDTO pista : todasLasPistas) {
            if (!pista.isDisponible()) {
                noDisponibles.add(pista);
            }
        }

        // Mostrar el mensaje correspondiente según el tamaño de la lista
        if (noDisponibles.isEmpty()) {
            System.out.println("No hay pistas no disponibles.");
        } else {
            System.out.println("Pistas no disponibles:");
            // Imprimir todas las pistas no disponibles
            for (PistaDTO pista : noDisponibles) {
                System.out.println(pista);
            }
        }

        return noDisponibles;
    }

    /**
     * Obtiene una lista de pistas disponibles para un número específico de jugadores y tipo de pista.
     *
     * @param numeroJugadores El número de jugadores que usarán la pista.
     * @param tamano          El tamaño de la pista deseada.
     * @return Una lista de pistas disponibles que cumplen con los requisitos.
     */
    public List<PistaDTO> obtenerPistasDisponibles(int numeroJugadores, PistaDTO.TamanoPista tamano) {
        List<PistaDTO> disponibles = new ArrayList<PistaDTO>();

        // Obtener todas las pistas y filtrar las disponibles
        List<PistaDTO> todasLasPistas = pistaDAO.obtenerTodasLasPistas();
        for (PistaDTO pista : todasLasPistas) {
            if (pista.getTamano() == tamano &&
                pista.isDisponible() &&
                pista.getMaxJugadores() >= numeroJugadores) {
                disponibles.add(pista);
            }
        }

        if (disponibles.isEmpty()) {
            System.out.println("No hay pistas disponibles que cumplan con los requisitos.");
        } else {
            System.out.println("Pistas disponibles:");
            for (PistaDTO pista : disponibles) {
                System.out.println(pista);
            }
        }

        return disponibles;
    }

    /**
     * Busca un material en la base de datos por su ID.
     *
     * @param idMaterial El ID del material a buscar.
     * @return El material encontrado, o null si no se encuentra.
     */
    public MaterialDTO buscarMaterialPorID(int idMaterial) {
        return materialDAO.obtenerMaterialPorID(idMaterial);  // Llama al DAO para obtener el material por su ID
    }

    /**
     * Busca una pista en la base de datos por su nombre.
     *
     * @param nombre El nombre de la pista a buscar.
     * @return La pista encontrada, o null si no se encuentra.
     */
    public PistaDTO buscarPistaPorNombre(String nombre) {
        return pistaDAO.obtenerPistaConMateriales(nombre);  // Llama al DAO para obtener la pista junto con sus materiales
    }

    /**
     * Elimina un material de una pista especificada.
     *
     * @param nombrePista El nombre de la pista.
     * @param idMaterial  El ID del material a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarMaterialDePista(String nombrePista, int idMaterial) {
        return pistaDAO.eliminarMaterialDePista(nombrePista, idMaterial);  // Llama al DAO para eliminar el material de la pista
    }

    // Métodos adicionales de persistencia

    /**
     * Obtiene todas las pistas de la base de datos.
     *
     * @return Una lista de todas las pistas.
     */
    public List<PistaDTO> obtenerTodasLasPistas() {
        return pistaDAO.obtenerTodasLasPistas();  // Llama al DAO para obtener todas las pistas
    }

    /**
     * Obtiene todos los materiales disponibles.
     *
     * @return Una lista de todos los materiales disponibles.
     */
    public List<MaterialDTO> obtenerMaterialesDisponibles() {
        return materialDAO.obtenerMaterialesDisponibles();  // Llama al DAO para obtener todos los materiales disponibles
    }
}
