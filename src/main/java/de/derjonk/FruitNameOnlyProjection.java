package de.derjonk;

import org.springframework.data.rest.core.config.Projection;

/**
 * Created by bej on 22/03/2017.
 */
@Projection(name = "fruitNameOnly", types = {Fruit.class})
public interface FruitNameOnlyProjection {

    String getName();
}
