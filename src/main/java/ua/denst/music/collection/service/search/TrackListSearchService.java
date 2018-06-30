package ua.denst.music.collection.service.search;

import ua.denst.music.collection.domain.dto.request.TrackListSearchRequestDto;
import ua.denst.music.collection.domain.dto.response.SearchAndDownloadResponseDto;

public interface TrackListSearchService {

    SearchAndDownloadResponseDto searchAndDownloadTrackList(final TrackListSearchRequestDto requestDto);

    void searchAndDownloadTrackListAsync(final TrackListSearchRequestDto requestDto);
}
