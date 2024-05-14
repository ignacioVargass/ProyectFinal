import java.time.LocalDateTime;

public class Country {
    private int country_id;
    private String country;
    private LocalDateTime last_update;

    public Country(int country_id, String country, LocalDateTime last_update) {
        this.country_id = country_id;
        this.country = country;
        this.last_update = last_update;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getLast_update() {
        return last_update;
    }

    public void setLast_update(LocalDateTime last_update) {
        this.last_update = last_update;
    }
}
