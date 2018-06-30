package ua.denst.music.collection.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.denst.music.collection.domain.entity.TrackListPattern;

@Repository
public interface TrackListPatternRepository extends CrudRepository<TrackListPattern, Long> {
}
