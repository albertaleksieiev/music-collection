package ua.denst.music.collection.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ua.denst.music.collection.domain.entity.VkAudio;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.Callable;

import static ua.denst.music.collection.util.Constants.PROXY_IP;
import static ua.denst.music.collection.util.Constants.PROXY_PORT;

@Slf4j
public class DownloadTask implements Callable<byte[]> {

    private final VkAudio audio;

    public DownloadTask(final VkAudio audio) {
        this.audio = audio;
    }

    @Override
    public byte[] call() throws Exception {
        log.info("Download started for {}", audio.getUrl());
        final Long startTs = System.currentTimeMillis();

        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_IP, PROXY_PORT));
        final HttpURLConnection connection = (HttpURLConnection) new URL(audio.getUrl()).openConnection(proxy);
        connection.setConnectTimeout(10_000);
        connection.setReadTimeout(10_000);

        final byte[] bytes = IOUtils.toByteArray(connection.getInputStream());
        log.info("Download finished, size - {}mb, time - {}sec", bytes.length / 1024 / 1024., (System.currentTimeMillis() - startTs) / 1000);
        return bytes;
    }

}
