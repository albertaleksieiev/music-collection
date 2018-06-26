package ua.denst.music.collection.domain.event;

import ua.denst.music.collection.domain.DownloadStatus;
import ua.denst.music.collection.domain.entity.VkAudio;

public abstract class DownloadStatusEvent extends DownloadEvent {
    DownloadStatusEvent(Object source) {
        super(source);
    }

    public abstract VkAudio getAudio();

    public abstract DownloadStatus getStatus();
}
