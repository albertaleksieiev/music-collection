package ua.denst.music.collection.domain.event;

import lombok.Getter;
import ua.denst.music.collection.domain.entity.VkAudio;

import java.util.Collections;
import java.util.List;

public class DownloadFinishEvent extends DownloadEvent {

    @Getter
    private final List<VkAudio> tracks;

    public DownloadFinishEvent(final Object source, final List<VkAudio> tracks) {
        super(source);
        this.tracks = Collections.unmodifiableList(tracks);
    }

}
