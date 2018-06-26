package ua.denst.music.collection.service;

public interface FileSystemImportService {
    Integer importCollection(final String collectionName, final boolean createCopyInEveryGenre);
}
