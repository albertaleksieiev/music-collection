package ua.denst.music.collection.client.impl;

import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import ua.denst.music.collection.client.DiscogsClient;

@Component
public class DiscogsClientImpl implements DiscogsClient {
    private static final String DISCOGS_API_BASE = "https://api.discogs.com/";
    private static final String DISCOGS_RELEASES = DISCOGS_API_BASE + "releases/";
    private static final String DISCOGS_MARKETPLACE_ITEM = DISCOGS_API_BASE + "marketplace/listings/";

    @Override
    @SneakyThrows
    public Connection.Response getRelease(final Long releaseId) {
        return Jsoup.connect(DISCOGS_RELEASES + releaseId)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
    }

    @Override
    @SneakyThrows
    public Connection.Response getMarketplaceItem(final Long itemId) {
        return Jsoup.connect(DISCOGS_MARKETPLACE_ITEM + itemId)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
    }
}
