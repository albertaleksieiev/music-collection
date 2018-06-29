package ua.denst.music.collection.domain.dto;

import lombok.Data;
import ua.denst.music.collection.domain.entity.Track;

import java.util.List;

@Data
public class DiscogsSearchAndDownloadResponseDto {
    //TODO TrackDto
    private List<Track> found;
    private List<String> notFound;
}
