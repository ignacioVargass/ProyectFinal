import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;

// Interfaz para los modelos de objetos relacionales
interface iORMObject {
    ResultSet Find(); // Método para buscar objetos
    ResultSet Find(Object id); // Método para buscar objetos por ID
    ResultSet Find(String search); // Método para buscar objetos por criterio de búsqueda
    ResultSet Find(LocalDateTime from, LocalDateTime to); // Método para buscar objetos por rango de fechas
    boolean Put(Map<String, String> data); // Método para actualizar objetos
    boolean Post(Map<String, String> data); // Método para agregar objetos
    boolean Delete(Object id); // Método para eliminar objetos por ID
}

// Clase base para los modelos de entidad
public class EntityModel implements iORMObject {
    private String tableName;
    private String primaryKey;
    private Connection connection;

    public EntityModel(String tableName, String primaryKey) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.connection = getConnection(); // Establecer conexión a la base de datos
    }

    // Método para obtener la conexión a la base de datos
    private Connection getConnection() {
        try {
            // Leer las propiedades de conexión desde un archivo de configuración
            Properties properties = getPropertiesFromFile("config.properties");
            String url = properties.getProperty("dburl");
            String username = properties.getProperty("dbuser");
            String password = properties.getProperty("dbpassword");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return null;
        }
    }

    // Método para leer las propiedades desde un archivo de configuración
    private Properties getPropertiesFromFile(String filename) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(filename)) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Error al cargar el archivo de configuración: " + ex.getMessage());
        }
        return properties;
    }

    // Implementación de los métodos de la interfaz iORMObject

    // Método para buscar objetos
    @Override
    public ResultSet Find() {
        String query = "SELECT * FROM " + tableName;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            return null;
        }
    }

    // Método para buscar objetos por ID
    @Override
    public ResultSet Find(Object id) {
        String query = "SELECT * FROM " + tableName + " WHERE " + primaryKey + " = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, id);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            return null;
        }
    }

    // Método para buscar objetos por criterio de búsqueda
    @Override
    public ResultSet Find(String search) {
        String query = "SELECT * FROM " + tableName + " WHERE country = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, search);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            return null;
        }
    }

    // Método para buscar objetos por rango de fechas
    @Override
    public ResultSet Find(LocalDateTime from, LocalDateTime to) {
        String query = "SELECT * FROM " + tableName + " WHERE set_time BETWEEN ? AND ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(2, to);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            return null;
        }
    }

    // Método para actualizar objetos
    @Override
    public boolean Put(Map<String, String> data) {
        String query = "UPDATE " + tableName + " SET ";
        for (String key : data.keySet()) {
            query += key + " = ?, ";
        }
        query = query.substring(0, query.length() - 2); // Eliminar la coma extra al final
        query += " WHERE " + primaryKey + " = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int index = 1;
            for (String value : data.values()) {
                statement.setString(index++, value);
            }
            // Establecer el valor del ID primario
            statement.setString(index, data.get(primaryKey));
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el objeto: " + e.getMessage());
            return false;
        }
    }

    // Método para agregar objetos
    @Override
    public boolean Post(Map<String, String> data) {
        String query = "INSERT INTO " + tableName + " (";
        String values = " VALUES (";
        for (String key : data.keySet()) {
            query += key + ", ";
            values += "?, ";
        }
        query = query.substring(0, query.length() - 2) + ")"; // Eliminar la coma extra al final y cerrar paréntesis
        values = values.substring(0, values.length() - 2) + ")"; // Eliminar la coma extra al final y cerrar paréntesis
        query += values;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int index = 1;
            for (String value : data.values()) {
                statement.setString(index++, value);
            }
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar el objeto: " + e.getMessage());
            return false;
        }
    }

    // Método para eliminar objetos por ID
    @Override
    public boolean Delete(Object id) {
        String query = "DELETE FROM " + tableName + " WHERE " + primaryKey + " = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el objeto: " + e.getMessage());
            return false;
        }
    }
}
