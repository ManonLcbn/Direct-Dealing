package eu.telecomnancy.base;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Signalement {
    private int id;
    private final StringProperty description = new SimpleStringProperty();


    public int getId() {
            return id;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setId(int id) {
            this.id = id;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Signalement(int id, String description) {
        this.id = id;
        this.description.set(description);
    }
}
