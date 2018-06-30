package ua.denst.music.collection.service;

import ua.denst.music.collection.domain.dto.response.SearchAndDownloadResponseDto;
import ua.denst.music.collection.domain.dto.response.DiscogsTrackListResponseDto;

import java.util.List;

public interface DiscogsService {
    SearchAndDownloadResponseDto searchAndDownload(String link, Long collectionId);

    List<DiscogsTrackListResponseDto> getTrackList(String link);
}
