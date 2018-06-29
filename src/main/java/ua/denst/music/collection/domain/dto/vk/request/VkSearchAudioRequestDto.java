package ua.denst.music.collection.domain.dto.vk.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VkSearchAudioRequestDto {
    private List<String> contentIds;
}
