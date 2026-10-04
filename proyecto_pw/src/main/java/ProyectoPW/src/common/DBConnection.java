package ProyectoPW.src.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

/**
 * Esta clase proporciona métodos para gestionar la conexión a la base de datos.
 * Permite obtener una conexión a la base de datos, cerrarla y cargar las propiedades de configuración necesarias.
 * Se conecta a una base de datos MySQL utilizando configuraciones externas especificadas en archivos de propiedades.
 * 
 * @author Guillermo Palacios García, José Trujillo Medina, Carlos Montero Hidalgo, Antonio Matas Muñoz
 * @version 1.0
 */
public class DBConnection {

    /**
     * Objeto que representa la conexión a la base de datos.
     */
    protected Connection connection = null;

    // Configuración de conexión (se cargará dinámicamente desde config.properties)
    /**
     * URL de conexión a la base de datos.
     */
    protected String url;

    /**
     * Nombre de usuario para la conexión a la base de datos.
     */
    protected String user;

    /**
     * Contraseña del usuario para la conexión a la base de datos.
     */
    protected String password;

    /**
     * Obtiene una conexión a la base de datos utilizando las configuraciones cargadas desde el archivo de propiedades.
     * 
     * @return La conexión a la base de datos.
     */
    public Connection getConnection() {
        try {
            // Cargar las propiedades de configuración desde config.properties
            Properties config = getConfigProperties();
            url = config.getProperty("db.url");
            user = config.getProperty("db.username");
            password = config.getProperty("db.password");

            // Cargar el driver y establecer la conexión
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection successfully opened!");
        } catch (SQLException e) {
            System.err.println("Connection to MySQL has failed!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found.");
            e.printStackTrace();
        }
        return this.connection;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection successfully closed!");
            }
        } catch (SQLException e) {
            System.err.println("Error while trying to close the connection.");
            e.printStackTrace();
        }
    }

    /**
     * Carga las propiedades SQL desde el archivo sql.properties.
     * 
     * @return Un objeto {@link Properties} que contiene las configuraciones de SQL.
     */
    public static Properties getSQLProperties() {
        Properties sql_properties = new Properties();
        try (InputStream sql_properties_file = 
                Thread.currentThread().getContextClassLoader().getResourceAsStream("sql.properties")) {
            if (sql_properties_file == null) {
                System.out.println("El archivo sql.properties no se encontró.");
                return sql_properties;
            }
            sql_properties.load(sql_properties_file);
        } catch (IOException e) {
            System.out.println("La conexión ha fallado");
            e.printStackTrace();
        }
        return sql_properties;
    }

    /**
     * Carga las propiedades de configuración desde el archivo config.properties.
     * 
     * @return Un objeto {@link Properties} que contiene las configuraciones generales.
     */
    public static Properties getConfigProperties() {
        Properties configProperties = new Properties();
        try (InputStream configFile = 
                Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            if (configFile == null) {
                System.out.println("El archivo config.properties no se encontró.");
                return configProperties;
            }
            configProperties.load(configFile);
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo config.properties");
            e.printStackTrace();
        }
        return configProperties;
    }
}
