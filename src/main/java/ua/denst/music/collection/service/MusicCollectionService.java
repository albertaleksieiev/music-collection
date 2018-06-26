package ua.denst.music.collection.service;

import ua.denst.music.collection.domain.entity.MusicCollection;

public interface MusicCollectionService {
    MusicCollection create(String name);

    MusicCollection findByName(String name);

    MusicCollection findById(Long collectionId);
}
