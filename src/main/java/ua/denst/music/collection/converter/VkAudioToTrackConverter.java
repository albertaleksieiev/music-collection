package ua.denst.music.collection.converter;

import org.springframework.stereotype.Component;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.entity.VkAudio;

@Component
public class VkAudioToTrackConverter {
    public Track convert(final VkAudio audio) {
        final Track track = new Track();

        track.setAuthorsStr(audio.getArtist());
        track.setTitle(audio.getTitle());
        track.setDuration(audio.getDuration());
        track.setBitRate(audio.getBitRate());
        track.setSizeMb(audio.getSizeMb());
        track.setFileName(audio.getFilename());

        return track;
    }
}
