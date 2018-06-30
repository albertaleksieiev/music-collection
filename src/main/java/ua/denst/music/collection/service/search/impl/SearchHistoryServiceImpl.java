package ua.denst.music.collection.service.search.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ua.denst.music.collection.domain.SearchStatus;
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.event.vk.VkDownloadFailEvent;
import ua.denst.music.collection.repository.SearchHistoryRepository;
import ua.denst.music.collection.service.search.SearchHistoryService;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

    SearchHistoryRepository repository;

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
}
