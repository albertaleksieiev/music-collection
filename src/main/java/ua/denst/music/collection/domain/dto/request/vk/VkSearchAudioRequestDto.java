package ua.denst.music.collection.domain.dto.request.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VkSearchAudioRequestDto {
    private List<String> contentIds;
}
