package ua.denst.music.collection.service;

import ua.denst.music.collection.domain.dto.DiscogsSearchAndDownloadResponseDto;
import ua.denst.music.collection.domain.dto.DiscogsTrackListResponseDto;

import java.util.List;

public interface DiscogsService {
    DiscogsSearchAndDownloadResponseDto searchAndDownload(String link, Long collectionId);

    List<DiscogsTrackListResponseDto> getTrackList(String link);
}
