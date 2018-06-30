package ua.denst.music.collection.service.search.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.denst.music.collection.domain.SearchStatus;
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.event.vk.VkDownloadFailEvent;
import ua.denst.music.collection.domain.event.vk.VkDownloadSuccessEvent;
import ua.denst.music.collection.repository.SearchHistoryRepository;
import ua.denst.music.collection.service.TrackService;
import ua.denst.music.collection.service.search.SearchHistoryService;

import java.util.Set;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Transactional
public class SearchHistoryServiceImpl implements SearchHistoryService {

    SearchHistoryRepository repository;
    TrackService trackService;

    @Override
    public SearchHistory save(final SearchHistory searchHistory) {
        return repository.save(searchHistory);
    }

    @Override
    public SearchHistory saveSuccess(final SearchHistory searchHistory, final Track track, final boolean existsInDb) {
        searchHistory.setSearchStatus(SearchStatus.SUCCESS);
        searchHistory.setTrack(track);
        searchHistory.setExistsInDb(existsInDb);
        return repository.save(searchHistory);
    }

    @Override
    public SearchHistory saveFail(final SearchHistory searchHistory) {
        searchHistory.setSearchStatus(SearchStatus.FAIL);
        return repository.save(searchHistory);
    }

    @Override
    public void onDownloadFailEvent(final VkDownloadFailEvent event) {
        saveFail(event.getSearchHistory());
    }

    @Override
    public void onDownloadSuccessEvent(final VkDownloadSuccessEvent event) {
        final Track track = event.getTrack();
        final Set<String> genres = event.getGenres();
        final Long collectionId = event.getCollectionId();

        final Track saved = trackService.save(track, genres, collectionId);

        saveSuccess(event.getSearchHistory(), saved, false);
    }
}
