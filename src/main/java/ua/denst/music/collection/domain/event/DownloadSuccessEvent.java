package ua.denst.music.collection.domain.event;

import lombok.Getter;
import ua.denst.music.collection.domain.DownloadStatus;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.entity.VkAudio;

import java.util.Set;

public class DownloadSuccessEvent extends DownloadStatusEvent {

    @Getter
    private final VkAudio audio;
    @Getter
    private final Track track;
    @Getter
    private final Set<String> genres;
    @Getter
    private final Long collectionId;

    public DownloadSuccessEvent(final Object source, final VkAudio audio, final Track track,
                                final Set<String> genres, final Long collectionId) {
        super(source);
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
