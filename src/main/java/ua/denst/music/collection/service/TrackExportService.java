package ua.denst.music.collection.service;

import ua.denst.music.collection.domain.entity.Track;

import java.io.File;

public interface TrackExportService {
    void exportTrack(final Track track, final File collectionFolder, final boolean createCopyInEveryGenreFolder);
}
