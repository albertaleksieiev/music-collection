package ua.denst.music.collection.domain.dto.response;

import lombok.Data;
import ua.denst.music.collection.domain.entity.Track;

import java.util.List;

@Data
public class SearchAndDownloadResponseDto {
    //TODO TrackDto
    private List<Track> found;
    private List<String> notFound;
}
