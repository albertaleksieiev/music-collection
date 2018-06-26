package ua.denst.music.collection.client.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import ua.denst.music.collection.client.VkClient;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static ua.denst.music.collection.util.Constants.PROXY_IP;
import static ua.denst.music.collection.util.Constants.PROXY_PORT;

@Slf4j
@Component
public class VkClientImpl implements VkClient {
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36";
    private static final String PATH_BASE = "https://vk.com";

    private final Map<String, String> cookies = new HashMap<>();

    @PostConstruct
    public void defaultCookies() {
        final Map<String, String> cookies = new HashMap<>();
        cookies.put("remixlang", "3");
        cookies.put("remixlhk", "df15b5c985b7556f55");
        cookies.put("remixstid", "1006736869_58b4810ff1d1246354");
        cookies.put("remixflash", "29.0.0");
        cookies.put("remixscreen_depth", "24");
        cookies.put("remixgp", "45842f2f68603c7d39011fb825f97747");
        cookies.put("remixdt", "0");
        cookies.put("remixrt", "1");
        cookies.put("tmr_detect", "0%7C1528040378581");
        cookies.put("remixsid", "fae1491f781ab0d9f9d4817bb43610a7beeaf413c8180493bf581");
        cookies.put("remixseenads", "0");
        cookies.put("remixcurr_audio", "15612655_456239099");

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
                .proxy(PROXY_IP, PROXY_PORT)
                .userAgent(USER_AGENT).cookies(cookies).method(method);
    }
}
