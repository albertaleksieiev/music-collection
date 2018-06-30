package ua.denst.music.collection.service.search;

import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;

import java.util.Optional;
import java.util.Set;

public interface VkSearchService {
    Optional<Track> searchAndDownload(final String authors, final String title, final Set<String> genres,
                                      final Long collectionId, final SearchHistory searchHistory);

    void searchAndDownloadAsync(final String authors, final String title, final Set<String> genres,
                                final Long collectionId, final SearchHistory searchHistory);
}
