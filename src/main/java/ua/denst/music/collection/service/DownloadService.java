package ua.denst.music.collection.service;

import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.entity.VkAudio;

import java.util.List;


public interface DownloadService {

    List<Track> download(List<VkAudio> audios);

    Track download(VkAudio vkAudio);

//    void download(Track track);

    void downloadAsync(List<VkAudio> audios);

}
