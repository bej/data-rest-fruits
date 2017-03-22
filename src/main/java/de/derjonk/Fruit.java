package de.derjonk;

import javax.persistence.*;

/**
 * Created by bej on 22/03/2017.
 */
@Entity
@Inheritance
public abstract class Fruit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Fruit() {

    }

    public Fruit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
