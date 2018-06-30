package ua.denst.music.collection.service.search;

import ua.denst.music.collection.domain.dto.request.SearchRequestDto;
import ua.denst.music.collection.domain.entity.Track;

import java.util.Optional;

public interface SearchServiceFacade {
    Optional<Track> searchAndDownload(final SearchRequestDto searchRequest);

    void searchAndDownloadAsync(final SearchRequestDto searchRequest);
}
