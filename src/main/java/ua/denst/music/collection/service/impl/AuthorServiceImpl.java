package ua.denst.music.collection.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.denst.music.collection.domain.entity.Author;
import ua.denst.music.collection.repository.AuthorRepository;
import ua.denst.music.collection.service.AuthorService;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;

    @Override
    public Set<Author> getOrCreateAuthors(final Set<String> authors) {
        final Set<Author> entities = new HashSet<>();

        authors.forEach(authorStr -> {
            final Author author = getOrCreate(authorStr);
            entities.add(author);
        });

        return entities;
    }

    private Author getOrCreate(final String authorStr) {
        final Author fromDb = authorRepository.findByNameIgnoreCase(authorStr);

        if (fromDb == null) {
            final Author newAuthor = new Author(authorStr);
            return authorRepository.save(newAuthor);
        }

        return fromDb;
    }
}
