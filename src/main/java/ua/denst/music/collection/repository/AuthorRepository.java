package ua.denst.music.collection.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.denst.music.collection.domain.entity.Author;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findByNameIgnoreCase(final String name);
}
