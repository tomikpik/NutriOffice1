package database.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;

/**
 * Created by Tom on 15. 5. 2015.
 */
@Entity
@Table(name = "Diet")
public class Diet {

    private SimpleIntegerProperty dietId = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "diet_id", unique = true, precision = 5, scale = 0)
    public int getDietId() {
        return dietId.get();
    }
    public void setDietId(int dietId) {
        this.dietId.set(dietId);
    }

    @Column(name = "diet_name", length = 100)
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
}