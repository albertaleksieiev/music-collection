package ua.denst.music.collection.domain.dto.vk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VkSearchResponseDto {
    private List<Object> list;
    private Integer nextOffset;
    private Boolean hasMore;
}
