package ua.denst.music.collection.service;

import ua.denst.music.collection.domain.entity.Author;

import java.util.Set;

public interface AuthorService {
    Set<Author> getOrCreateAuthors(final Set<String> authors);
}
