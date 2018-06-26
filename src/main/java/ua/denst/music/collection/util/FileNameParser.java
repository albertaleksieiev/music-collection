package ua.denst.music.collection.util;

import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.denst.music.collection.domain.FileNameData;

import java.util.HashSet;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Slf4j
@Component
public class FileNameParser {
    private static final String FILE_NAME_EXTENSION_SEPARATOR = ".";
    private static final String EMPTY_EXTENSION = "";
    private static final String DASH = "-";
    private static final String AUTHORS_SEPARATOR = "(&)|( x )|(,)";

    public FileNameData parseFileName(final String fileName) {
        final Pair<String, String> nameExtension = parseNameExtension(fileName);
        final Pair<String, Set<String>> titleAuthors = parseTitleAuthors(nameExtension.getKey());

        return new FileNameData(titleAuthors.getKey().trim(), titleAuthors.getValue(), nameExtension.getValue().trim());
    }

    public Set<String> parseAuthors(final String authorsString) {
        final Set<String> authors = new HashSet<>();

        final String[] split = authorsString.split(AUTHORS_SEPARATOR);
        if (split.length == 0) {
            authors.add(authorsString.trim());
        } else {
            for (final String author : split) {
                authors.add(author.trim());
            }
        }

        return authors;
    }

    private Pair<String, String> parseNameExtension(final String fileName) {
        final int indexOfStartExtension = fileName.lastIndexOf(FILE_NAME_EXTENSION_SEPARATOR);

        if (indexOfStartExtension < 0) {
            log.warn("Cant parse extension from filename {}", fileName);
            return new Pair<>(fileName, EMPTY_EXTENSION);
        } else {
            final String title = fileName.substring(0, indexOfStartExtension);
            final String extension = fileName.substring(indexOfStartExtension + 1);
            return new Pair<>(title, extension);
        }
    }

    //TODO correct processing of Author - Title (author remix)
    private Pair<String, Set<String>> parseTitleAuthors(final String fileNameWithoutExtension) {
        final String normalized = normalize(fileNameWithoutExtension);

        final int indexOfDash = normalized.lastIndexOf(DASH);

        if (indexOfDash < 0) {
            return new Pair<>(fileNameWithoutExtension, new HashSet<>());
        } else {
            final String authorsString = fileNameWithoutExtension.substring(0, indexOfDash);
            final String title = fileNameWithoutExtension.substring(indexOfDash + 1);

            final Set<String> authors = parseAuthors(authorsString);

            return new Pair<>(title, authors);
        }
    }

    private String normalize(final String fileNameWithoutExtension) {
        String normalized = fileNameWithoutExtension.trim();

        if (fileNameWithoutExtension.endsWith(DASH)) {
            normalized = fileNameWithoutExtension.substring(0, fileNameWithoutExtension.length() - 1);
        }

        return normalized;
    }
}
