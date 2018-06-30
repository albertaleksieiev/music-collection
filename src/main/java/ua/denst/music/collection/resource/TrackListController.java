package ua.denst.music.collection.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.denst.music.collection.domain.dto.request.TrackListSearchRequestDto;
import ua.denst.music.collection.domain.dto.response.SearchAndDownloadResponseDto;
import ua.denst.music.collection.service.search.TrackListSearchService;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(value = "/api/search/tracklist")
public class TrackListController {

    TrackListSearchService trackListSearchService;

    @PostMapping
    public ResponseEntity<SearchAndDownloadResponseDto> searchTrackList(@RequestBody final TrackListSearchRequestDto searchRequestDto,
                                                                        @RequestParam(name = "async", defaultValue = "true") final boolean async) {
        if (async) {
            trackListSearchService.searchAndDownloadTrackListAsync(searchRequestDto);
            return ResponseEntity.ok().build();
        }
        final SearchAndDownloadResponseDto response = trackListSearchService.searchAndDownloadTrackList(searchRequestDto);

        return ResponseEntity.ok(response);
    }
}
