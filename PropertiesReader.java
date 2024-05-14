import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
    private static final String CONFIG_FILE = "config.properties";

    public static Properties getProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de configuración: " + e.getMessage());
        }
        return properties;
    }
}
