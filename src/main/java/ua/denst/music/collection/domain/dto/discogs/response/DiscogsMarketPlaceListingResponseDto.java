package ua.denst.music.collection.domain.dto.discogs.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscogsMarketPlaceListingResponseDto {
    private DiscogsReleaseDto release;
}
