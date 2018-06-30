package ua.denst.music.collection.domain.event.vk;

import lombok.Getter;
import ua.denst.music.collection.domain.entity.VkAudio;

public class VkDownloadFinishEvent extends DownloadEvent {

    @Getter
    private final VkAudio track;

    public VkDownloadFinishEvent(final Object source, final VkAudio track) {
        super(source);
        this.track = track;
    }

}
