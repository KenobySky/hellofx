package br.sky.models;

/**
 *
 * @author Andre Lopes
 */
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Fruit implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = true, length = 100)
    private String color;

    public Fruit() {
    }

    public Fruit(long id, String color) {
        this.id = id;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
