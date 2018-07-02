package ua.denst.music.collection.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@org.springframework.context.annotation.Configuration
@PropertySource("classpath:env.properties")
@ConfigurationProperties(prefix = "configuration")
public class Configuration {
    public static class Proxy {
        @Getter @Setter private String ip;
        @Getter @Setter private Integer port;
    }
    public static class Vk {
        private String userId;

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Long getUserId() {
            return Long.parseLong(userId);
        }
    }
    public static class Connection {
        @Getter @Setter private String vk_cookie;
    }

    @Getter @Setter private Proxy proxy;
    @Getter @Setter private Vk vk;
    @Getter @Setter private Connection connection;
}
