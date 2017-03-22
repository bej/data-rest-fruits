package de.derjonk;

import javax.persistence.Entity;

@Entity
public class Orange extends Fruit {

    public Orange() {

    }

    public Orange(final String name) {
        super(name);
    }
}
