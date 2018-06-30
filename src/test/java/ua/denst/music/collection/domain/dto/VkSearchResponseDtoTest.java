package ua.denst.music.collection.domain.dto;

import org.junit.Test;
import ua.denst.music.collection.domain.dto.response.vk.VkSearchResponseDto;
import ua.denst.music.collection.util.JsonUtils;
import ua.denst.music.collection.util.ResourceLoader;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;

public class VkSearchResponseDtoTest {

    @Test
    public void shouldDeserialize() throws IOException, URISyntaxException {
        final String content = ResourceLoader.loadResource("vk/search.json");

        final VkSearchResponseDto vkSearchResponseDto = JsonUtils.fromString(content, VkSearchResponseDto.class);

        //TODO all fields
        assertNotNull(vkSearchResponseDto);
    }
}