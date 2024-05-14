import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFile {
    private Properties configProps;

    // Constructor sin argumentos
    public PropertyFile() {
        this.configProps = new Properties();
        // Puedes inicializar el archivo de propiedades aquí si es necesario
    }

    public PropertyFile(String filename) {
        // Carga las propiedades desde el archivo especificado
        this();
        try (InputStream input = new FileInputStream(filename)) {
            configProps.load(input);
        } catch (IOException ex) {
            System.err.println("Error al cargar el archivo de propiedades: " + ex.getMessage());
        }
    }

    public String getPropValue(String key) {
        // No es necesario verificar si la clave existe, getProperty() ya maneja eso
        return configProps.getProperty(key);
    }
}
