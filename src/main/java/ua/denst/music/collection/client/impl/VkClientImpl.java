package ua.denst.music.collection.client.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.denst.music.collection.client.VkClient;
import ua.denst.music.collection.util.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class VkClientImpl implements VkClient {
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36";
    private static final String PATH_BASE = "https://vk.com";


    private Configuration configuration;

    private final Map<String, String> cookies = new HashMap<>();

    @Autowired
    public VkClientImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @PostConstruct
    public void defaultCookies() {
        final Map<String, String> cookies = new HashMap<>();

        String[] cookiesArray = configuration.getConnection().getVkCookie().split(";");
        for (String cookie : cookiesArray) {
            String[] split = cookie.split("=");
            cookies.put(split[0].trim(), split[1].trim());
        }

        setCookies(cookies);
    }

    @Override
    public void addCookies(Map<String, String> cookies) {
        this.cookies.putAll(cookies);
    }

    @Override
    public void setCookies(Map<String, String> cookies) {
        clearCookies();
        addCookies(cookies);
    }

    @Override
    public void clearCookies() {
        this.cookies.clear();
    }

    @Override
    @SneakyThrows
    public Connection.Response search(final Long ownerId, final String searchQuery, final Integer offset) {
        return proxiedConnection(PATH_BASE + "/al_audio.php", Connection.Method.POST)
                .data("access_hash", "")
                .data("act", "load_section")
                .data("al", "1")
                .data("claim", "0")
                .data("offset", String.valueOf(offset))
                .data("owner_id", ownerId.toString())
                .data("search_history", "0")
                .data("type", "search")
                .data("search_q", searchQuery)
                .execute();
    }

    @Override
    @SneakyThrows
    public Connection.Response reload(final String audioIds) {
        return proxiedConnection(PATH_BASE + "/al_audio.php", Connection.Method.POST)
                .data("act", "reload_audio")
                .data("al", "1")
                .data("ids", audioIds)
                .execute();
    }

    @Override
    @SneakyThrows
    public Connection.Response callUrl(final String url) {
        return proxiedConnection(url, Connection.Method.GET)
                .ignoreContentType(true)
                .header("range", "bytes=0-1")
                .execute();
    }

    private Connection proxiedConnection(final String url, final Connection.Method method) {
        return Jsoup.connect(url)
                .proxy(configuration.getProxy().getIp(), configuration.getProxy().getPort())
                .userAgent(USER_AGENT).cookies(cookies).method(method);
    }
}
