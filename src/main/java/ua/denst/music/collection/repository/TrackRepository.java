package ua.denst.music.collection.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.denst.music.collection.domain.entity.Track;

@Repository
public interface TrackRepository extends PagingAndSortingRepository<Track, Long> {
    Page<Track> findByCollections_CollectionId(final Long collectionId, final Pageable page);

    Track findByAuthorsStrIgnoreCaseAndTitleIgnoreCase(final String artist, final String title);
}
