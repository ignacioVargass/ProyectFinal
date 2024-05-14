package OracleCurseTutor2.Dato;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.File;

public class Entity {
    private String ActionMessage;
    private Connection oConn;
    private Statement stmQry;
    private String _vCatalog;

    public Entity() {
        try {
            PropertyFile objsetting = new PropertyFile();
            String sdriver = objsetting.getPropValue("dbdriver");
            Class.forName(sdriver).newInstance();
            String dburl = objsetting.getPropValue("dburl");
            _vCatalog = objsetting.getPropValue("dbcatalog");
            dburl += "?user=" + objsetting.getPropValue("dbuser");
            dburl += "&password=" + objsetting.getPropValue("dbpassword");
            oConn = DriverManager.getConnection(dburl);
            stmQry = oConn.createStatement();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            ActionMessage = e.getMessage();
        }
    }

    public ResultSet getData(String p_strSQLStm) {
        try {
            ResultSet rlSet = stmQry.executeQuery(p_strSQLStm);
            return rlSet;
        } catch (SQLException e) {
            ActionMessage = e.toString();
            return null;
        } catch (Exception e) {
            ActionMessage = e.toString();
            return null;
        }
    }

    public void CloseDB() {
        try {
            oConn.close();
            stmQry.close();
        } catch (Exception e) {
        }
    }

    public String getActionMessage() {
        return ActionMessage;
    }

    private static class PropertyFile {
        private Properties configProps = null;
    
        public PropertyFile() {
            try {
                String filePath = "C:\\Users\\IgnacioVargas\\.vscode\\Proyectos\\OracleCurseTutor2\\config.properties";
                File configFile = new File(filePath);
                if (configFile.exists()) {
                    FileReader reader = new FileReader(configFile);
                    configProps = new Properties();
                    configProps.load(reader);
                    reader.close();
                } else {
                    System.err.println("Error: El archivo config.properties no se encontró en la ruta especificada.");
                }
            } catch (IOException ex) {
                System.err.println("Error al cargar el archivo de configuración: " + ex.getMessage());
            }
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
        Entity entity = new Entity();
        // Por ejemplo, podrías llamar a métodos de la clase Entity o realizar alguna operación específica
        System.out.println("ActionMessage: " + entity.getActionMessage());
    }
}
