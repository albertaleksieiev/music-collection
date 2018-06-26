package ua.denst.music.collection.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.denst.music.collection.domain.entity.Property;

@Repository
public interface PropertyRepository extends CrudRepository<Property, String> {
}
