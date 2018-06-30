package ua.denst.music.collection.service.search;

import org.springframework.context.event.EventListener;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.domain.event.vk.VkDownloadStatusEvent;

public interface VkAudioService {

    void save(VkAudio audio);

    @EventListener
    void onDownloadStatusEvent(VkDownloadStatusEvent event);

}
