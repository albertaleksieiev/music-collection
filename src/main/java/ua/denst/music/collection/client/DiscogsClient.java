package ua.denst.music.collection.client;

import org.jsoup.Connection;

public interface DiscogsClient {
    Connection.Response getRelease(Long releaseId);

    Connection.Response getMarketplaceItem(Long itemId);
}
