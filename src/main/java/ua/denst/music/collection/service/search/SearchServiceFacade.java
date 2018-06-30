package ua.denst.music.collection.service.search;

import ua.denst.music.collection.domain.entity.Track;

import java.util.Optional;
import java.util.Set;

public interface SearchServiceFacade {
    Optional<Track> searchAndDownload(final String authors, final String title,
                                      final Set<String> genres, final Long collectionId);

    void searchAndDownloadAsync(final String authors, final String title,
                                final Set<String> genres, final Long collectionId);
}
