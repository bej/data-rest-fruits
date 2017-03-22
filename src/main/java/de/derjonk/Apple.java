package de.derjonk;

import javax.persistence.Entity;

@Entity
public class Apple extends Fruit {

    public Apple() {

    }

    public Apple(final String name) {
        super(name);
    }
}
