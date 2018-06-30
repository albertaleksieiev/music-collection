package ua.denst.music.collection.service.search.vk;

import org.springframework.stereotype.Component;
import ua.denst.music.collection.domain.dto.request.vk.VkSearchAudioRequestDto;
import ua.denst.music.collection.domain.dto.response.vk.VkSearchResponseDto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class VkSearchResultProcessor {
    public VkSearchAudioRequestDto processSearchResults(final List<VkSearchResponseDto> searchResult) {
        final List<String> contentIds = new ArrayList<>();

        if (!searchResult.isEmpty()) {
            searchResult.forEach(vkSearchResponseDto -> {
                processOneSearchResult(contentIds, vkSearchResponseDto);
            });
        }

        final VkSearchAudioRequestDto vkSearchAudioRequestDto = new VkSearchAudioRequestDto();
        vkSearchAudioRequestDto.setContentIds(contentIds);

        return vkSearchAudioRequestDto;
    }

    private void processOneSearchResult(final List<String> contentIds, final VkSearchResponseDto vkSearchResponseDto) {
        final List<Object> trackInfos = vkSearchResponseDto.getList();

        trackInfos.forEach(trackInfo -> {
            final ArrayList trackFields = (ArrayList) trackInfo;

            processFields(contentIds, trackFields);
        });
    }

    private void processFields(final List<String> contentIds, final ArrayList trackFields) {
        if (trackFields != null) {
            final LinkedHashMap props = (LinkedHashMap) trackFields.get(15);

            if (props != null && (Integer.valueOf(String.valueOf(props.get("puid22"))) == 7)) {
                contentIds.add(String.valueOf(props.get("content_id")));
            }
        }
    }
}
