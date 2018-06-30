package ua.denst.music.collection.service.search;

import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.entity.VkAudio;

import java.util.Optional;
import java.util.Set;


public interface VkDownloadService {

    Optional<Track> download(VkAudio vkAudio, Set<String> genres, Long collectionId, SearchHistory searchHistory);

    void downloadAsync(VkAudio audio, Set<String> genres, Long collectionId, SearchHistory searchHistory);

}
