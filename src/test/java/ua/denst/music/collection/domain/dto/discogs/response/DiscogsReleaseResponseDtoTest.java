package ua.denst.music.collection.domain.dto.discogs.response;

import org.junit.Test;
import ua.denst.music.collection.domain.dto.response.discogs.DiscogsReleaseResponseDto;
import ua.denst.music.collection.util.JsonUtils;
import ua.denst.music.collection.util.ResourceLoader;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class DiscogsReleaseResponseDtoTest {
    @Test
    public void shouldDeserialize() throws IOException, URISyntaxException {
        final String content = ResourceLoader.loadResource("discogs/release.json");

        final DiscogsReleaseResponseDto release = JsonUtils.fromString(content, DiscogsReleaseResponseDto.class);

        assertNotNull(release);
    }
}