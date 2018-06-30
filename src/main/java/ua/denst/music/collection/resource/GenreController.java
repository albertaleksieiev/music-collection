package ua.denst.music.collection.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.denst.music.collection.domain.entity.Genre;
import ua.denst.music.collection.service.GenreService;

@AllArgsConstructor
@RestController
@CrossOrigin
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(value = "/api/genres")
public class GenreController {
    GenreService genreService;

    @PostMapping(params = "name")
    public ResponseEntity<Genre> create(@RequestParam(name = "name") final String name) {
        final Genre genre = genreService.create(name);

        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<Genre>> findAll() {
        final Iterable<Genre> genres = genreService.findAll();

        return new ResponseEntity<>(genres, HttpStatus.OK);
    }
}
