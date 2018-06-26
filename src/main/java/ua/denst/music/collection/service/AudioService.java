package ua.denst.music.collection.service;

import org.springframework.context.event.EventListener;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.domain.event.DownloadStatusEvent;

public interface AudioService {

//    List<VkAudio> findAll();

//    List<VkAudio> findFailed();

//    List<VkAudio> fetchAll();

    void save(VkAudio audio);

    @EventListener
    void onDownloadStatusEvent(DownloadStatusEvent event);

}
