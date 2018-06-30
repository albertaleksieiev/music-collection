package ua.denst.music.collection.service.impl;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ua.denst.music.collection.domain.entity.Genre;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.service.TrackImportService;

import java.io.File;
import java.util.Set;

@Service
public class TrackImportServiceImpl implements TrackImportService {

    @Override
    @SneakyThrows
    public void importTrack(final Track track, final File collectionFolder, final boolean createCopyInEveryGenreFolder) {
        final Set<Genre> genres = track.getGenres();

        if (!CollectionUtils.isEmpty(genres)) {
            if (createCopyInEveryGenreFolder) {
                genres.forEach(genre -> createFileInGenreFolder(track, collectionFolder, genre));
            } else {
                final Genre firstGenre = genres.stream().findFirst().get();
                createFileInGenreFolder(track, collectionFolder, firstGenre);
            }
        }
    }

    @SneakyThrows
    private void createFileInGenreFolder(final Track track, final File collectionFolder, final Genre genre) {
        final String genreName = genre.getName();
        final File genreFolder = new File(collectionFolder, genreName);
        genreFolder.mkdir();

        final File trackFile = new File(genreFolder, track.getFileName());
        final byte[] content = track.getContent().getContent();
        FileUtils.writeByteArrayToFile(trackFile, content);
    }
}
