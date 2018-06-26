package ua.denst.music.collection.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.denst.music.collection.domain.entity.MusicCollection;
import ua.denst.music.collection.service.MusicCollectionService;

@AllArgsConstructor
@RestController
@CrossOrigin
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(value = "/api/collections")
public class MusicCollectionController {
    MusicCollectionService musicCollectionService;

    @PostMapping(params = "name")
    public ResponseEntity<MusicCollection> create(@RequestParam(name = "name") String name) {
        final MusicCollection collection = musicCollectionService.create(name);

        return new ResponseEntity<>(collection, HttpStatus.OK);
    }
}
