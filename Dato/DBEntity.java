import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;

public class DBEntity {
    private String ActionMessage;
    private String _vCatalog;
    private Properties configProps;

    // Constructor sin argumentos
    public DBEntity() {
        this.configProps = new PropertyFile().getConfigProps();
        // Aquí puedes establecer valores predeterminados para las propiedades, si es necesario
    }

    // Constructor que toma un objeto Properties
    public DBEntity(Properties configProps) {
        this.configProps = configProps;
        try {
            Class.forName(configProps.getProperty("dbdriver")).newInstance();
            _vCatalog = configProps.getProperty("dbcatalog");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            ActionMessage = "Error loading driver: " + e.getMessage();
        } catch (Exception e) {
            ActionMessage = e.getMessage();
        }
    }

    public ResultSet getData(String p_strSQLStm) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(p_strSQLStm)) {
            return stmt.executeQuery();
        } catch (SQLException e) {
            ActionMessage = e.getMessage();
            return null;
        }
    }

    public ResultSet getMeta() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
        } catch (SQLException e) {
            ActionMessage = e.getMessage();
            return null;
        } finally {
            closeConnection(conn);
        }
    }

    public void ShowDbObject(Scanner pin) {
        try (ResultSet oTbl = getMeta()) {
            // Code to show database objects
        } catch (Exception e) {
            System.out.println(e.toString() + ActionMessage + ".... continue!!!:");
            pin.next();
        }
    }

    public String getActionMessage() {
        return ActionMessage;
    }

    private Connection getConnection() throws SQLException {
        PropertyFile objsetting = new PropertyFile();
        String dburl = objsetting.getPropValue("dburl") + "?useSSL=false";
        dburl += "&user=" + objsetting.getPropValue("dbuser");
        dburl += "&password=" + objsetting.getPropValue("dbpassword");
        try {
            Class.forName(objsetting.getPropValue("dbdriver")).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new SQLException("Error loading driver: " + e.getMessage());
        }
        return DriverManager.getConnection(dburl);
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                ActionMessage = e.getMessage();
            }
        }
    }

    private static class PropertyFile {
        private Properties configProps = null;
    
        public PropertyFile() {
            try {
                String filePath = "C:\\Users\\IgnacioVargas\\.vscode\\Proyectos\\OracleCurseTutor2\\config.properties";
                FileInputStream input = new FileInputStream(filePath);
                configProps = new Properties();
                configProps.load(input);
                input.close();
            } catch (IOException ex) {
                System.err.println("Error al cargar el archivo de configuración: " + ex.getMessage());
            }
        }
    
        public Properties getConfigProps() {
            return configProps;
        }
    
        public String getPropValue(String key) {
            String keyvalue = "";
            if (configProps != null && configProps.containsKey(key)) {
                keyvalue = configProps.getProperty(key);
            }
            return keyvalue;
        }
    }
    public static void main(String[] args) {
        // Aquí puedes incluir el código que desees ejecutar al correr esta clase como programa principal
        DBEntity entity = new DBEntity();
        // Por ejemplo, podrías llamar a métodos de la clase DBEntity o realizar alguna operación específica
        System.out.println("ActionMessage: " + entity.getActionMessage());
    }
}

