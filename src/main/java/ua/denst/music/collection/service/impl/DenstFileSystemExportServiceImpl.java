package ua.denst.music.collection.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.denst.music.collection.domain.entity.MusicCollection;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.exception.ExportException;
import ua.denst.music.collection.repository.TrackRepository;
import ua.denst.music.collection.service.FileSystemExportService;
import ua.denst.music.collection.service.MusicCollectionService;
import ua.denst.music.collection.service.TrackExportService;
import ua.denst.music.collection.util.Configuration;

import java.io.File;

@Service
@AllArgsConstructor
@Slf4j
public class DenstFileSystemExportServiceImpl implements FileSystemExportService {

    private final TrackRepository trackRepository;
    private final TrackExportService trackExportService;
    private final MusicCollectionService collectionService;
    private Configuration configuration;

    @Value("${music.collection.import.root.folder}")
    private String rootFolder;

    @Autowired
    public DenstFileSystemExportServiceImpl(final TrackRepository trackRepository,
                                            final TrackExportService trackExportService,
                                            final MusicCollectionService collectionService) {
        this.trackRepository = trackRepository;
        this.trackExportService = trackExportService;
        this.collectionService = collectionService;
    }

    @Override
    public Integer exportCollection(final String collectionName, final boolean createCopyInEveryGenre) {
        final MusicCollection collection = collectionService.findByName(collectionName);

        if (collection == null) {
            throw new ExportException("Collection with name \"" + collectionName + "\" not exists.");
        }

        final File root = new File(rootFolder);

        if (!root.exists() && !root.mkdirs()) {
            throw new IllegalStateException("Can not create destination folder.");
        }

        final File collectionFolder = new File(root, collectionName);
        collectionFolder.mkdir();

        Integer countExported = 0;

        final Page<Track> firstPage = trackRepository.findByCollections_CollectionId(collection.getCollectionId(), new PageRequest(0, 20));
        countExported += exportPage(firstPage, collectionFolder, createCopyInEveryGenre);
        for (int i = 1; i < firstPage.getTotalPages(); i++) {
            final Page<Track> page = trackRepository.findByCollections_CollectionId(collection.getCollectionId(), new PageRequest(i, 20));
            countExported += exportPage(page, collectionFolder, createCopyInEveryGenre);
        }

        return countExported;
    }

    private Integer exportPage(final Page<Track> page, final File collectionFolder, final boolean createCopyInEveryGenre) {
        Integer exported = 0;

        for (final Track track : page) {
            log.info("Exporting track {}", track.getFileName());
            trackExportService.exportTrack(track, collectionFolder, createCopyInEveryGenre);
            exported++;
        }

        return exported;
    }


}
