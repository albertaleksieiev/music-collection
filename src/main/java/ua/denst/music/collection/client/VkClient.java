package ua.denst.music.collection.client;

import org.jsoup.Connection;

import java.util.Map;

public interface VkClient {

    void setCookies(Map<String, String> cookies);

    void addCookies(Map<String, String> cookies);

    void clearCookies();

    Connection.Response search(Long ownerId, String searchQuery, Integer offset);

    Connection.Response reload(String audioIds);

    Connection.Response callUrl(String url);

}
