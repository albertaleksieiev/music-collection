package ua.denst.music.collection.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.denst.music.collection.client.DownloadTask;
import ua.denst.music.collection.converter.VkAudioToTrackConverter;
import ua.denst.music.collection.domain.TrackSource;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.entity.TrackContent;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.domain.event.DownloadFailEvent;
import ua.denst.music.collection.domain.event.DownloadFinishEvent;
import ua.denst.music.collection.domain.event.DownloadSuccessEvent;
import ua.denst.music.collection.exception.DownloadException;
import ua.denst.music.collection.property.DownloaderProperties;
import ua.denst.music.collection.service.DownloadService;
import ua.denst.music.collection.service.PropertyService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VkDownloadService implements DownloadService {
    private static final String MP3_EXTENSION = "mp3";

    ApplicationEventPublisher publisher;
    PropertyService propertyService;
    VkAudioToTrackConverter converter;

    @Async
    @Override
    public void downloadAsync(final List<VkAudio> audios) {
        download(audios);
    }

    @Override
    public List<Track> download(final List<VkAudio> audios) {
        log.debug("Download queue: {}", audios.size());

        final DownloaderProperties properties = propertyService.get(DownloaderProperties.class);
        return download(properties, audios);
    }

    @Override
    public Track download(final VkAudio vkAudio) {
        final List<Track> tracks = download(Collections.singletonList(vkAudio));

        return tracks.get(0);
    }

    @SneakyThrows
    private List<Track> download(final DownloaderProperties properties, final List<VkAudio> audios) {
        final List<Track> tracks = new ArrayList<>();
        log.debug("Download pool-size: {}", properties.getPoolSize());

        final ExecutorService executor = Executors.newFixedThreadPool(properties.getPoolSize());
        final CompletionService<byte[]> completionService = new ExecutorCompletionService<>(executor);

        for (final VkAudio audio : audios) {
            completionService.submit(new DownloadTask(audio));
            downloadTrack(tracks, completionService, audio);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        publisher.publishEvent(new DownloadFinishEvent(this, audios));

        return tracks;
    }

    private void downloadTrack(final List<Track> tracks, final CompletionService<byte[]> completionService, final VkAudio audio) throws InterruptedException {
        try {
            final byte[] content = completionService.take().get();
            publisher.publishEvent(new DownloadSuccessEvent(this, audio));

            final Track track = createTrack(audio, content);
            tracks.add(track);
        } catch (final ExecutionException ex) {
            publisher.publishEvent(new DownloadFailEvent(this, (DownloadException) ex.getCause()));
        }
    }

    private Track createTrack(final VkAudio audio, final byte[] content) {
        final Track track = converter.convert(audio);
        final TrackContent trackContent = new TrackContent();
        trackContent.setContent(content);
        track.setContent(trackContent);
        track.setTrackSource(TrackSource.VK);
        track.setExtension(MP3_EXTENSION);
        return track;
    }
}
