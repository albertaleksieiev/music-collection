package ua.denst.music.collection.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import static ua.denst.music.collection.util.Constants.JSON_DELIMITER;


@Component
public class JsonUtils implements ApplicationContextAware {
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        mapper = applicationContext.getBean(ObjectMapper.class);
    }

    @SneakyThrows
    public static String toString(final Object obj) {
        return mapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T fromString(final String json, final Class<T> clazz) {
        return mapper.readValue(json, clazz);
    }

    public static <T> T responseToObject(final String body, final Class<T> clazz) {
        String json = body.substring(body.indexOf(JSON_DELIMITER) + JSON_DELIMITER.length());
        json = json.substring(0, json.indexOf("<!>"));
        return JsonUtils.fromString(json, clazz);
    }
}
