package ua.denst.music.collection.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.denst.music.collection.domain.entity.MusicCollection;
import ua.denst.music.collection.exception.ExistsException;
import ua.denst.music.collection.repository.MusicCollectionRepository;
import ua.denst.music.collection.service.MusicCollectionService;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class MusicCollectionServiceImpl implements MusicCollectionService {
    public static final String DEFAULT_COLLECTION_NAME = "Default Collection";

    private final MusicCollectionRepository repository;

//    @PostConstruct
    public void init() {
        create(DEFAULT_COLLECTION_NAME);
    }

    @Override
    public MusicCollection create(final String name) {
        if (exists(name)) {
            throw new ExistsException("Music collection with name \"" + name + "\" already exists.");
        }

        final MusicCollection collection = new MusicCollection(name);

        return repository.save(collection);
    }

    @Override
    public MusicCollection findByName(final String name) {
        return repository.findByCollectionNameIgnoreCase(name);
    }

    @Override
    public MusicCollection findById(final Long collectionId) {
        return repository.findOne(collectionId);
    }

    private boolean exists(final String name) {
        return repository.findByCollectionNameIgnoreCase(name) != null;
    }

}
