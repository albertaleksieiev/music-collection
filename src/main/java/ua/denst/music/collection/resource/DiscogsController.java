package ua.denst.music.collection.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.denst.music.collection.domain.dto.response.SearchAndDownloadResponseDto;
import ua.denst.music.collection.domain.dto.response.DiscogsTrackListResponseDto;
import ua.denst.music.collection.service.DiscogsService;

import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(value = "/api/discogs")
public class DiscogsController {
    DiscogsService discogsService;

    @PostMapping(params = {"link", "collectionId"})
    public ResponseEntity<SearchAndDownloadResponseDto> downloadByDiscogsLink(@RequestParam(name = "link") final String link,
                                                                              @RequestParam(name = "collectionId") final Long collectionId) {
        final SearchAndDownloadResponseDto result = discogsService.searchAndDownload(link, collectionId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(params = "link")
    public ResponseEntity<List<DiscogsTrackListResponseDto>> getTrackListByDiscogsLink(@RequestParam(name = "link") final String link) {
        final List<DiscogsTrackListResponseDto> trackList = discogsService.getTrackList(link);

        return new ResponseEntity<>(trackList, HttpStatus.OK);
    }
}
