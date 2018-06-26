package ua.denst.music.collection.domain.event;

import lombok.Getter;
import ua.denst.music.collection.domain.DownloadStatus;
import ua.denst.music.collection.domain.entity.VkAudio;

public class DownloadSuccessEvent extends DownloadStatusEvent {

    @Getter
    private final VkAudio audio;

    public DownloadSuccessEvent(final Object source, final VkAudio track) {
        super(source);
        this.audio = track;
    }

    @Override
    public DownloadStatus getStatus() {
        return DownloadStatus.SUCCESS;
    }
}
