package ua.denst.music.collection.domain.event.vk;

import lombok.Getter;
import ua.denst.music.collection.domain.DownloadStatus;
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.exception.DownloadException;

public class VkDownloadFailEvent extends VkDownloadStatusEvent {

    @Getter
    private final DownloadException cause;

    public VkDownloadFailEvent(final Object source, final DownloadException cause, final SearchHistory searchHistory) {
        super(source, searchHistory);
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
