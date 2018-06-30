package ua.denst.music.collection.domain.event.vk;

import lombok.Getter;
import ua.denst.music.collection.domain.DownloadStatus;
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.VkAudio;

public abstract class VkDownloadStatusEvent extends DownloadEvent {
    @Getter
    protected final SearchHistory searchHistory;

    VkDownloadStatusEvent(final Object source, final SearchHistory searchHistory) {
        super(source);
        this.searchHistory = searchHistory;
    }

    public abstract VkAudio getAudio();

    public abstract DownloadStatus getStatus();
}
