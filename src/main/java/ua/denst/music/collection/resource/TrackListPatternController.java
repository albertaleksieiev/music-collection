package ua.denst.music.collection.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.denst.music.collection.domain.dto.request.TrackListPatternDto;
import ua.denst.music.collection.domain.entity.TrackListPattern;
import ua.denst.music.collection.service.search.TrackListPatternService;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(value = "/api/search/tracklist/pattern")
public class TrackListPatternController {

    TrackListPatternService trackListPatternService;

    @PostMapping
    public ResponseEntity<TrackListPattern> createPattern(@RequestBody final TrackListPatternDto patternDto) {
        final TrackListPattern trackListPattern = trackListPatternService.create(patternDto.getRegex(), patternDto.getExample());

        return ResponseEntity.ok(trackListPattern);
    }
}
