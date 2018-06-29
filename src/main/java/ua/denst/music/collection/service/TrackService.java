package ua.denst.music.collection.service;

import org.springframework.context.event.EventListener;
import ua.denst.music.collection.domain.TrackSource;
import ua.denst.music.collection.domain.entity.Author;
import ua.denst.music.collection.domain.entity.MusicCollection;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.event.DownloadSuccessEvent;

import java.util.Set;

public interface TrackService {
    Track create(final String title, final String fileName, final Set<Author> authors, final String extension,
                 final byte[] content, final Double sizeMb, final Short bitRate, final TrackSource trackSource,
                 final Set<String> genres, final MusicCollection collection);

    Track save(final Track track, final Set<String> genres, final Long collectionId);

    Track findByArtistAndTitle(final String artist, final String title);

    @EventListener
    void onDownloadSuccessEvent(DownloadSuccessEvent event);
}
