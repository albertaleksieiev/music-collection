package ua.denst.music.collection.domain.event;

import lombok.Getter;
import ua.denst.music.collection.domain.entity.VkAudio;

public class DownloadFinishEvent extends DownloadEvent {

    @Getter
    private final VkAudio track;

    public DownloadFinishEvent(final Object source, final VkAudio track) {
        super(source);
        this.track = track;
    }

}
