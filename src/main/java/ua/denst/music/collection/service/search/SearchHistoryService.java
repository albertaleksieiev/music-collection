package ua.denst.music.collection.service.search;

import org.springframework.context.event.EventListener;
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.event.vk.VkDownloadFailEvent;
import ua.denst.music.collection.domain.event.vk.VkDownloadSuccessEvent;

public interface SearchHistoryService {
    SearchHistory save(SearchHistory searchHistory);

    SearchHistory saveSuccess(SearchHistory searchHistory, Track track, boolean existsInDb);

    SearchHistory saveFail(SearchHistory searchHistory);

    @EventListener
    void onDownloadFailEvent(VkDownloadFailEvent event);

    @EventListener
    void onDownloadSuccessEvent(VkDownloadSuccessEvent event);

    Integer clearSuccessEvents();
}
