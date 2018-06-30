package ua.denst.music.collection.service.search.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.denst.music.collection.domain.entity.MusicCollection;
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.service.MusicCollectionService;
import ua.denst.music.collection.service.TrackService;
import ua.denst.music.collection.service.search.SearchHistoryService;
import ua.denst.music.collection.service.search.SearchServiceFacade;
import ua.denst.music.collection.service.search.VkSearchService;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static ua.denst.music.collection.service.impl.MusicCollectionServiceImpl.DEFAULT_COLLECTION_NAME;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Transactional
@Slf4j
public class SearchServiceFacadeImpl implements SearchServiceFacade {

    SearchHistoryService searchHistoryService;
    VkSearchService vkSearchService;
    TrackService trackService;
    MusicCollectionService musicCollectionService;

    //    @PostConstruct
    public void checkSearch() {
        final MusicCollection defaultCollection = musicCollectionService.findByName(DEFAULT_COLLECTION_NAME);
        final Set<String> genres = Collections.singleton("Electro");

        searchAndDownload("Claro Intelecto", "Tone", genres, defaultCollection.getCollectionId());
    }

    @Override
    public Optional<Track> searchAndDownload(final String authors, final String title, final Set<String> genres, final Long collectionId) {
        final SearchHistory searchHistory = createSearchHistory(authors, title);

        final Track fromDb = trackService.findByArtistAndTitle(authors, title);
        if (fromDb != null) {
            log.debug("Found track in db, not perform searching.");
            searchHistoryService.saveSuccess(searchHistory, fromDb, true);
            return Optional.of(fromDb);
        }

        return vkSearchService.searchAndDownload(authors, title, genres, collectionId, searchHistory);
    }

    @Override
    public void searchAndDownloadAsync(final String authors, final String title, final Set<String> genres, final Long collectionId) {
        final SearchHistory searchHistory = createSearchHistory(authors, title);

        final Track fromDb = trackService.findByArtistAndTitle(authors, title);
        if (fromDb != null) {
            log.debug("Found track in db, not perform searching.");
            searchHistoryService.saveSuccess(searchHistory, fromDb, true);
        } else {
            vkSearchService.searchAndDownloadAsync(authors, title, genres, collectionId, searchHistory);
        }
    }

    private SearchHistory createSearchHistory(final String authors, final String title) {
        final SearchHistory searchHistory = new SearchHistory();
        searchHistory.setArtists(authors);
        searchHistory.setTitle(title);
        return searchHistory;
    }
}
