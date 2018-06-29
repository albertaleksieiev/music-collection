package ua.denst.music.collection.domain.dto.discogs.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscogsTrackDto {
    private String duration;
    private String position;
    private String type_;
    private String title;
    private List<DiscogsArtistDto> artists;
}
