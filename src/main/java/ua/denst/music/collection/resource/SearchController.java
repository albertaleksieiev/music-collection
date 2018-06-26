package ua.denst.music.collection.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.service.SearchService;

import java.util.Optional;
import java.util.Set;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(value = "/api/search")
public class SearchController {
    SearchService searchService;

    @PostMapping(params = {"artist", "title", "genres", "collectionId"})
    public ResponseEntity<Track> search(@RequestParam(name = "artist") final String artist,
                                        @RequestParam(name = "title") final String title,
                                        @RequestParam(name = "genres") final Set<String> genres,
                                        @RequestParam(name = "collectionId") final Long collectionId) {
        final Optional<Track> track = searchService.search(artist, title, genres, collectionId);

        return track.map(track1 -> new ResponseEntity<>(track1, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
