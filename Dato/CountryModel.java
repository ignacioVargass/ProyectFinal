import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CountryModel extends EntityModel implements iORMObject {
    private ArrayList<Country> allData;

    public CountryModel() {
        super("Country", "country_id");
    }

    @Override
    public void Mapping(ResultSet rSet) {
        short id;
        String sname;
        LocalDateTime dt;
        allData = new ArrayList<>();
        try {
            while (rSet.next()) {
                id = rSet.getShort("country_id");
                sname = rSet.getString("country");
                dt = rSet.getObject("last_update", LocalDateTime.class);
                Country objCountry = new Country(id, sname, dt); // Asumiendo que Country es una clase definida
                allData.add(objCountry);
            }
            rSet.close();
        } catch (SQLException e) {
            // Corrección: Utilizamos el método setActionMessage() de la clase base EntityModel
            setActionMessage(e.getMessage());
        }
    }

    @Override
    public ResultSet Find(Object id) {
        // Corrección: Corregimos la llamada al método Find de la clase base EntityModel
        String query = "SELECT * FROM Country WHERE country_id = ?";
        return super.Find(query, id);
    }

    @Override
    public ResultSet Find(String search) {
        // Corrección: Corregimos la llamada al método Find de la clase base EntityModel
        String query = "SELECT * FROM Country WHERE country LIKE ?";
        return super.Find(query, "%" + search + "%");
    }

    @Override
    public ResultSet Find(LocalDateTime from, LocalDateTime to) {
        // Corrección: Corregimos la llamada al método Find de la clase base EntityModel
        String query = "SELECT * FROM Country WHERE last_update BETWEEN ? AND ?";
        return super.Find(query, from, to);
    }

    @Override
    public ResultSet Find() {
        // Corrección: Corregimos la llamada al método Find de la clase base EntityModel
        String query = "SELECT * FROM Country";
        return super.Find(query);
    }

    // Otros métodos...

    @Override
    public Map<String, String> SerializerMap(Entity odata) {
        if (!(odata instanceof Country)) {
            return null;
        }
        Country objCountry = (Country) odata;
        Map<String, String> countryMap = new HashMap<>();
        countryMap.put("country_id", Integer.toString(objCountry.getCountry_id())); // Convertido a String
        countryMap.put("country", objCountry.getCountry());
        countryMap.put("last_update", LocalDateTime.now().toString()); // Se usa LocalDateTime.now() para obtener la fecha y hora actual
        return countryMap;
    }

    @Override
    public String Serializer() {
        StringBuilder sb = new StringBuilder();
        for (Country c : allData) {
            sb.append("[");
            sb.append(c.getCountry_id());
            sb.append(",");
            sb.append(c.getCountry());
            sb.append(",");
            sb.append(c.getLast_update());
            sb.append("],");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
