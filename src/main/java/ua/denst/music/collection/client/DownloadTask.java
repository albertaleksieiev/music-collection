package ua.denst.music.collection.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.util.Configuration;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.Callable;


@Slf4j
public class DownloadTask implements Callable<byte[]> {

    private final VkAudio audio;
    Configuration configuration;

    public DownloadTask(VkAudio audio, Configuration configuration) {
        this.audio = audio;
        this.configuration = configuration;
    }

    @Override
    public byte[] call() throws Exception {
        log.info("Download started for {}", audio.getUrl());
        final Long startTs = System.currentTimeMillis();

        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(configuration.getProxy().getIp(), configuration.getProxy().getPort()));
        final HttpURLConnection connection = (HttpURLConnection) new URL(audio.getUrl()).openConnection(proxy);
        connection.setConnectTimeout(10_000);
        connection.setReadTimeout(10_000);

        final byte[] bytes = IOUtils.toByteArray(connection.getInputStream());
        log.info("Download finished, size - {}mb, time - {}sec", bytes.length / 1024 / 1024., (System.currentTimeMillis() - startTs) / 1000);
        return bytes;
    }

}
