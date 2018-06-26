package ua.denst.music.collection.domain.dto;

import org.junit.Test;
import ua.denst.music.collection.util.JsonUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;

public class VkSearchResponseDtoTest {

    @Test
    public void shouldDeserialize() throws IOException, URISyntaxException {
        final URL resource = getClass().getClassLoader().getResource("search.json");
        final Path path = Paths.get(resource.toURI());
        final String content = new String(Files.readAllBytes(path));

        final VkSearchResponseDto vkSearchResponseDto = JsonUtils.fromString(content, VkSearchResponseDto.class);

        assertNotNull(vkSearchResponseDto);
    }
}