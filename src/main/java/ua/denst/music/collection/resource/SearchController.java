package ua.denst.music.collection.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.denst.music.collection.domain.dto.request.SearchRequestDto;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.service.search.SearchHistoryService;
import ua.denst.music.collection.service.search.SearchServiceFacade;

import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(value = "/api/search")
public class SearchController {

    SearchServiceFacade searchServiceFacade;
    SearchHistoryService searchHistoryService;

    @PostMapping
    public ResponseEntity<Track> searchAndDownload(@RequestBody final SearchRequestDto searchRequest,
                                                   @RequestParam(name = "async", defaultValue = "false") final boolean async) {
        if (async) {
            searchServiceFacade.searchAndDownloadAsync(searchRequest);

            return ResponseEntity.ok().build();
        }

        final Optional<Track> track = searchServiceFacade.searchAndDownload(searchRequest);

        return track.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/history")
    public ResponseEntity<Integer> clearHistory() {
        return ResponseEntity.ok(searchHistoryService.clearSuccessEvents());
    }
}
