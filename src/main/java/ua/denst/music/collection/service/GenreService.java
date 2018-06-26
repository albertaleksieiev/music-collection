package ua.denst.music.collection.service;

import ua.denst.music.collection.domain.entity.Genre;

public interface GenreService {
    Genre create(String name);

    Genre findByName(String name);

    Iterable<Genre> findAll();
}
