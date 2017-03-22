package de.derjonk;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = FruitNameOnlyProjection.class)
public interface FruitRepository extends CrudRepository<Fruit, Long> {
}
