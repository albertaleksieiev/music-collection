package ua.denst.music.collection.service;

import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.entity.VkAudio;

import java.util.Optional;
import java.util.Set;


public interface DownloadService {

    Optional<Track> download(VkAudio vkAudio, Set<String> genres, Long collectionId);

    void downloadAsync(VkAudio audio, Set<String> genres, Long collectionId);

}
