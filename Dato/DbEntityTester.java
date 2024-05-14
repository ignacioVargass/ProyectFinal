import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class DbEntityTester {

    private static final String CONFIG_FILE_PATH = "C:\\Users\\IgnacioVargas\\.vscode\\Proyectos\\OracleCurseTutor2\\config.properties";

    public static void main(String[] args) {
        try {
            // Cargar las propiedades de configuración desde el archivo
            Properties configProps = new Properties();
            configProps.load(new FileInputStream(CONFIG_FILE_PATH));

            // Establecer la conexión a la base de datos
            Connection connection = DriverManager.getConnection(configProps.getProperty("dburl"),
                    configProps.getProperty("dbuser"), configProps.getProperty("dbpassword"));

            // Obtener metadatos de las tablas
            System.out.println("Database catalog:");
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                System.out.println(tables.getString("TABLE_NAME"));
            }

            // Solicitar al usuario el nombre de la tabla
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the table name: ");
            String tableName = reader.readLine();

            // Obtener y mostrar los datos de la tabla especificada
            System.out.println("Data in the table:");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData resultMetaData = resultSet.getMetaData();
            int columnCount = resultMetaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", resultMetaData.getColumnName(i));
            }
            System.out.println();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-20s", resultSet.getObject(i));
                }
                System.out.println();
            }

            // Cerrar recursos
            resultSet.close();
            statement.close();
            connection.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
