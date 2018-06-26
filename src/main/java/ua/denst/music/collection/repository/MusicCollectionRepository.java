package ua.denst.music.collection.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.denst.music.collection.domain.entity.MusicCollection;

@Repository
public interface MusicCollectionRepository extends CrudRepository<MusicCollection, Long> {
    MusicCollection findByCollectionNameIgnoreCase(final String collectionName);
}
