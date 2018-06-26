package ua.denst.music.collection.domain.event;

import lombok.Getter;
import ua.denst.music.collection.domain.DownloadStatus;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.exception.DownloadException;

public class DownloadFailEvent extends DownloadStatusEvent {

    @Getter
    private final DownloadException cause;

    public DownloadFailEvent(Object source, DownloadException cause) {
        super(source);
        this.cause = cause;
    }

    @Override
    public VkAudio getAudio() {
        return cause.getAudio();
    }

    @Override
    public DownloadStatus getStatus() {
        return DownloadStatus.FAIL;
    }
}
