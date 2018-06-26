package ua.denst.music.collection.service;

public interface FileSystemExportService {
    Integer syncCollectionFromFileSystem(final String collectionName, final String root, final Short bitRate);
}
