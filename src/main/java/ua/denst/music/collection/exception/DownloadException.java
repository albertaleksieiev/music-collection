package ua.denst.music.collection.exception;

import lombok.Getter;
import ua.denst.music.collection.domain.entity.VkAudio;

public class DownloadException extends Exception {

    @Getter
    private final VkAudio audio;

    public DownloadException(final Throwable cause, final VkAudio audio) {
        super(cause);
        this.audio = audio;
    }
}
