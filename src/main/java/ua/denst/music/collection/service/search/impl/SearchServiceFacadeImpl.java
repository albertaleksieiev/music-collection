package ua.denst.music.collection.service.search.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.denst.music.collection.domain.dto.request.SearchRequestDto;
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.service.TrackService;
import ua.denst.music.collection.service.search.SearchHistoryService;
import ua.denst.music.collection.service.search.SearchServiceFacade;
import ua.denst.music.collection.service.search.VkSearchService;

import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Transactional
@Slf4j
public class SearchServiceFacadeImpl implements SearchServiceFacade {

    SearchHistoryService searchHistoryService;
    VkSearchService vkSearchService;
    TrackService trackService;

    @Override
    public Optional<Track> searchAndDownload(final SearchRequestDto searchRequest) {
        final String authors = searchRequest.getArtists();
        final String title = searchRequest.getTitle();

        final SearchHistory searchHistory = createSearchHistory(authors, title);

        final Track fromDb = trackService.findByArtistAndTitle(authors, title);
        if (fromDb != null) {
            log.debug("Found track in db, not perform searching.");
            searchHistoryService.saveSuccess(searchHistory, fromDb, true);
            return Optional.of(fromDb);
        }

        return vkSearchService.searchAndDownload(authors, title, searchRequest.getGenres(), searchRequest.getCollectionId(), searchHistory);
    }

    @Override
    @Async
    public void searchAndDownloadAsync(final SearchRequestDto searchRequest) {
        final String authors = searchRequest.getArtists();
        final String title = searchRequest.getTitle();

        final SearchHistory searchHistory = createSearchHistory(authors, title);

        final Track fromDb = trackService.findByArtistAndTitle(authors, title);
        if (fromDb != null) {
            log.debug("Found track in db, not perform searching.");
            searchHistoryService.saveSuccess(searchHistory, fromDb, true);
        } else {
            vkSearchService.searchAndDownloadAsync(authors, title, searchRequest.getGenres(), searchRequest.getCollectionId(), searchHistory);
        }
    }

    private SearchHistory createSearchHistory(final String authors, final String title) {
        final SearchHistory searchHistory = new SearchHistory();
        searchHistory.setArtists(authors);
        searchHistory.setTitle(title);
        return searchHistory;
    }
}
