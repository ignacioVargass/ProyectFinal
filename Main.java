package OracleCurseTutor2.Dato;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Ejemplo de uso de la clase Entity
        useEntity();

        // Ejemplo de uso de la clase EntityModel
        useEntityModel();

        // Ejemplo de uso de la clase CountryModel
        useCountryModel();
    }

    private static void useEntity() {
        // Crear una instancia de Entity
        Entity entity = new Entity();

        // Ejemplo: Obtener datos mediante una consulta SQL
        ResultSet resultSet = entity.getData("SELECT * FROM some_table");
        // Manejar el ResultSet según sea necesario
        // Por ejemplo, recorrer los resultados e imprimirlos
        try {
            while (resultSet.next()) {
                // Manejar los datos del resultado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar recursos, como el ResultSet y la conexión
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                entity.CloseDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Imprimir mensaje de acción
        System.out.println("ActionMessage: " + entity.getActionMessage());
    }

    private static void useEntityModel() {
        // Crear una instancia de EntityModel
        EntityModel entityModel = new EntityModel("SomeTable", "id");

        // Ejemplo: Obtener todos los datos de la tabla
        ResultSet resultSet = entityModel.Find();
        // Manejar el ResultSet según sea necesario
        // Por ejemplo, recorrer los resultados e imprimirlos
        try {
            while (resultSet.next()) {
                // Manejar los datos del resultado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar recursos, como el ResultSet y la conexión
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                // No olvides cerrar la conexión de EntityModel
                // entityModel.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void useCountryModel() {
        // Crear una instancia de CountryModel
        CountryModel countryModel = new CountryModel();

        // Ejemplo: Obtener todos los datos de la tabla "Country"
        ResultSet resultSet = countryModel.Find();
        // Manejar el ResultSet según sea necesario
        // Por ejemplo, recorrer los resultados e imprimirlos
        try {
            while (resultSet.next()) {
                // Obtener los datos de la fila actual
                int countryId = resultSet.getInt("country_id");
                String countryName = resultSet.getString("country");
                LocalDateTime lastUpdate = resultSet.getObject("last_update", LocalDateTime.class);

                // Crear una instancia de Country con los datos obtenidos
                Country country = new Country(countryId, countryName, lastUpdate);

                // Agregar el objeto Country a la lista de datos de CountryModel
                // (En este ejemplo, asumimos que la lista allData de CountryModel es pública)
                countryModel.allData.add(country);

                // Imprimir los datos para verificar
                System.out.println(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar recursos, como el ResultSet y la conexión
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                // No olvides cerrar la conexión de CountryModel si es necesario
                // countryModel.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
