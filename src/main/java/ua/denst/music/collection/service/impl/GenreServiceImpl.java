package ua.denst.music.collection.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ua.denst.music.collection.domain.entity.Genre;
import ua.denst.music.collection.exception.ExistsException;
import ua.denst.music.collection.repository.GenreRepository;
import ua.denst.music.collection.service.GenreService;

import javax.annotation.PostConstruct;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    GenreRepository repository;

    @PostConstruct
    public void init() {
        create("Electro");
        create("Acid");
        create("Techno");
        create("EBM");
        create("Industrial");
        create("Minimal");
        create("House");
        create("Tech house");
        create("Deep house");
        create("Breaks");
    }

    @Override
    public Genre create(final String name) {

        if (exists(name)) {
            throw new ExistsException("Genre with name \"" + name + "\" already exists.");
        }

        final Genre genre = new Genre(name);

        return repository.save(genre);
    }

    @Override
    public Genre findByName(final String name) {
        return repository.findByNameIgnoreCase(name);
    }

    @Override
    public Iterable<Genre> findAll() {
        return repository.findAll();
    }

    private boolean exists(final String name) {
        return repository.findByNameIgnoreCase(name) != null;
    }
}
