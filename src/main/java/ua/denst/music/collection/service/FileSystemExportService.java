package ua.denst.music.collection.service;

public interface FileSystemExportService {
    Integer exportCollection(final String collectionName, final boolean createCopyInEveryGenre);
}
