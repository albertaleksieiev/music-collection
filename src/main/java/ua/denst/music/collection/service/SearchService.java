package ua.denst.music.collection.service;

import ua.denst.music.collection.domain.entity.Track;

import java.util.Optional;
import java.util.Set;

public interface SearchService {
    Optional<Track> searchAndDownload(final String authors, final String title,
                                      final Set<String> genres, final Long collectionId);

    void searchAndDownloadAsync(final String authors, final String title,
                                           final Set<String> genres, final Long collectionId);
}
