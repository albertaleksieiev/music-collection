package ua.denst.music.collection.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.denst.music.collection.domain.entity.Genre;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {
    Genre findByNameIgnoreCase(final String name);
}
