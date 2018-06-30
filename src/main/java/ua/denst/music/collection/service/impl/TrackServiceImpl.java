package ua.denst.music.collection.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ua.denst.music.collection.domain.TrackSource;
import ua.denst.music.collection.domain.entity.*;
import ua.denst.music.collection.repository.GenreRepository;
import ua.denst.music.collection.repository.MusicCollectionRepository;
import ua.denst.music.collection.repository.TrackRepository;
import ua.denst.music.collection.service.AuthorService;
import ua.denst.music.collection.service.TrackService;
import ua.denst.music.collection.util.FileNameParser;

import java.util.HashSet;
import java.util.Set;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class TrackServiceImpl implements TrackService {
    TrackRepository trackRepository;
    GenreRepository genreRepository;
    MusicCollectionRepository collectionRepository;

    AuthorService authorService;
    FileNameParser fileNameParser;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Track create(final String title, final String fileName, final Set<Author> authors, final String extension,
                        final byte[] content, final Double sizeMb, final Short bitRate, final TrackSource trackSource,
                        final Set<String> genreNames, final MusicCollection collection) {

        final Set<Genre> genres = getGenres(genreNames);

        final Track track = new Track();
        track.setTitle(title);
        track.setFileName(fileName);
        track.setAuthors(authors);
        track.setExtension(extension);
        setContent(content, track);
        track.setBitRate(bitRate);
        track.setTrackSource(trackSource);
        track.setGenres(genres);
        track.setSizeMb(sizeMb);
        track.setCollections(new HashSet<MusicCollection>() {{
            add(collection);
        }});

        return trackRepository.save(track);
    }

    private void setContent(final byte[] content, final Track track) {
        final TrackContent trackContent = new TrackContent();
        trackContent.setContent(content);
        track.setContent(trackContent);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Track save(final Track track, final Set<String> genres, final Long collectionId) {

        if (StringUtils.isNotEmpty(track.getAuthorsStr()) && (CollectionUtils.isEmpty(track.getAuthors()))) {
            setAuthors(track.getAuthorsStr(), track);
        }

        track.setGenres(getGenres(genres));

        setCollections(track, collectionId);

        return trackRepository.save(track);
    }

    @Override
    public Track findByArtistAndTitle(final String artist, final String title) {
        return trackRepository.findByAuthorsStrIgnoreCaseAndTitleIgnoreCase(artist, title);
    }

    private void setAuthors(final String authorsStr, final Track track) {
        final Set<String> authorsString = fileNameParser.parseAuthors(authorsStr);
        final Set<Author> authors = authorService.getOrCreateAuthors(authorsString);
        track.setAuthors(authors);
    }

    private Set<Genre> getGenres(final Set<String> genreNames) {
        final Set<Genre> genres = new HashSet<>();

        if (genreNames != null) {
            genreNames.forEach(genreName -> {
                final Genre genre = getOrCreate(genreName);
                genres.add(genre);
            });
        }

        return genres;
    }

    private Genre getOrCreate(final String genreName) {
        Genre genre = genreRepository.findByNameIgnoreCase(genreName);

        if (genre == null) {
            genre = new Genre(genreName);
        }
        return genre;
    }

    private void setCollections(final Track track, final Long collectionId/*final MusicCollection collection*/) {
        final MusicCollection collection = collectionRepository.findOne(collectionId);
        if (collection != null) {
            final Set<MusicCollection> collections = new HashSet<>();
            collections.add(collection);
            track.setCollections(collections);
        } else {
            log.warn("Collection with id {} not found.", collectionId);
        }
    }
}
