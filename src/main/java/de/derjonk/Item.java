package de.derjonk;

import javax.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Fruit fruit;

    public Fruit getFruit() {
        return fruit;
    }

    public void setFruit(final Fruit fruit) {
        this.fruit = fruit;
    }
}
