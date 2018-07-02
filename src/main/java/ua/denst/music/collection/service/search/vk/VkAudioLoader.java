package ua.denst.music.collection.service.search.vk;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.denst.music.collection.client.VkClient;
import ua.denst.music.collection.domain.dto.request.vk.VkSearchAudioRequestDto;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.util.Configuration;
import ua.denst.music.collection.util.JsonUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static ua.denst.music.collection.util.Constants.JSON_DELIMITER;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VkAudioLoader {
    static int SLEEP_INTERVAL = 5_000;

    VkClient vkClient;
    ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
    String script;
    Configuration configuration;

    @Autowired
    public VkAudioLoader(final VkClient vkClient, Configuration configuration) throws IOException {
        this.vkClient = vkClient;
        this.configuration = configuration;
        script = loadDecryptionScript();
    }

    private String loadDecryptionScript() throws IOException {
        final StringBuilder sb = new StringBuilder();

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("decrypt.js")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public List<VkAudio> load(final VkSearchAudioRequestDto vkSearchAudioRequestDto) throws InterruptedException {
        final List<VkAudio> result = new ArrayList<>();

        final int size = vkSearchAudioRequestDto.getContentIds().size();
        if (size > 0) {
            int sleepInterval = SLEEP_INTERVAL;
            int fromIndex = 0;
            int toIndex = Math.min(fromIndex + 10, size);

            while (fromIndex != toIndex) {
                log.debug("Fetching urls: {} - {}", fromIndex, toIndex);
                final String ids = StringUtils.join(vkSearchAudioRequestDto.getContentIds()
                        .subList(fromIndex, toIndex), ",");

                final Connection.Response response = vkClient.reload(ids);

                final String body = response.body();
                if (!body.contains(JSON_DELIMITER)) {
                    log.info("Sleeping {} sec...", sleepInterval / 1000);
                    Thread.sleep(sleepInterval);
                    sleepInterval += SLEEP_INTERVAL;
                    continue;
                } else if (sleepInterval != SLEEP_INTERVAL) {
                    sleepInterval = SLEEP_INTERVAL;
                }

                final List<List> lists = JsonUtils.responseToObject(body, List.class);

                for (final List object : lists) {
                    createVkAudio(result, object);
                }

                // sleeping
                Thread.sleep(200);
                fromIndex = toIndex;
                toIndex = Math.min(fromIndex + 10, vkSearchAudioRequestDto.getContentIds().size());
            }
        }
        return result;
    }

    private void createVkAudio(final List<VkAudio> result, final List object) {
        final Long id = ((Number) object.get(0)).longValue();
        final String url = (String) object.get(2);
        final String title = (String) object.get(3);
        final String authors = (String) object.get(4);
        final Integer duration = (Integer) object.get(5);

        if (url != null && !url.isEmpty()) {
            final VkAudio track = new VkAudio(id, configuration.getVk().getUserId(), authors, title, duration);
            track.setUrl(decrypt(url));
            result.add(track);
        } else {
            log.info("Url for track {} is empty, skipping processing", authors + " - " + title);
        }
    }

    @SneakyThrows
    private String decrypt(final String url) {
        final String script = this.script.replace("${vkId}", configuration.getVk().getUserId().toString()); // TODO: replace with bindings
        scriptEngine.eval(script);

        final Invocable inv = (Invocable) scriptEngine;
        return (String) inv.invokeFunction("decode", url);
    }
}
