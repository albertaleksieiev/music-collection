package ua.denst.music.collection.domain.event.vk;

import lombok.Getter;
import ua.denst.music.collection.domain.DownloadStatus;
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.entity.VkAudio;

import java.util.Set;

public class VkDownloadSuccessEvent extends VkDownloadStatusEvent {

    @Getter
    private final VkAudio audio;
    @Getter
    private final Track track;
    @Getter
    private final Set<String> genres;
    @Getter
    private final Long collectionId;

    public VkDownloadSuccessEvent(final Object source, final VkAudio audio, final Track track,
                                  final Set<String> genres, final Long collectionId, final SearchHistory searchHistory) {
        super(source, searchHistory);
        this.audio = audio;
        this.track = track;
        this.genres = genres;
        this.collectionId = collectionId;
    }

    @Override
    public DownloadStatus getStatus() {
        return DownloadStatus.SUCCESS;
    }


}
