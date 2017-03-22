package de.derjonk;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface OrangeRepository extends CrudRepository<Orange, Long> {
}
