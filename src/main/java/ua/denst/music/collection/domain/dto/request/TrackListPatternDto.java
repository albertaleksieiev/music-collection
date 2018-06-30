package ua.denst.music.collection.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackListPatternDto {
    private final String regex;
    private final String example;
}
