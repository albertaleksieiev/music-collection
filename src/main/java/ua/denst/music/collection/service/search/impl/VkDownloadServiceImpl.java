package ua.denst.music.collection.service.search.impl;

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
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.entity.TrackContent;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.domain.event.vk.VkDownloadFailEvent;
import ua.denst.music.collection.domain.event.vk.VkDownloadFinishEvent;
import ua.denst.music.collection.domain.event.vk.VkDownloadSuccessEvent;
import ua.denst.music.collection.exception.DownloadException;
import ua.denst.music.collection.property.DownloaderProperties;
import ua.denst.music.collection.service.PropertyService;
import ua.denst.music.collection.service.search.VkDownloadService;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VkDownloadServiceImpl implements VkDownloadService {
    private static final String MP3_EXTENSION = "mp3";

    ApplicationEventPublisher publisher;
    PropertyService propertyService;
    VkAudioToTrackConverter converter;

    @Async
    @Override
    public void downloadAsync(final VkAudio audio, final Set<String> genres, final Long collectionId, final SearchHistory searchHistory) {
        download(audio, genres, collectionId, searchHistory);
    }

    @Override
    public Optional<Track> download(final VkAudio vkAudio, final Set<String> genres, final Long collectionId,
                                    final SearchHistory searchHistory) {
        final DownloaderProperties properties = propertyService.get(DownloaderProperties.class);
        return download(properties, vkAudio, genres, collectionId, searchHistory);
    }

    @SneakyThrows
    private Optional<Track> download(final DownloaderProperties properties, final VkAudio audio,
                                     final Set<String> genres, final Long collectionId, final SearchHistory searchHistory) {
        log.debug("Download pool-size: {}", properties.getPoolSize());

        final ExecutorService executor = Executors.newFixedThreadPool(properties.getPoolSize());
        final CompletionService<byte[]> completionService = new ExecutorCompletionService<>(executor);

        completionService.submit(new DownloadTask(audio));
        final Optional<Track> track = downloadTrack(completionService, audio, genres, collectionId, searchHistory);

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        publisher.publishEvent(new VkDownloadFinishEvent(this, audio));

        return track;
    }

    private Optional<Track> downloadTrack(final CompletionService<byte[]> completionService, final VkAudio audio,
                                          final Set<String> genres, final Long collectionId, final SearchHistory searchHistory) throws InterruptedException {
        try {
            final byte[] content = completionService.take().get();

            final Track track = createTrack(audio, content);
            publisher.publishEvent(new VkDownloadSuccessEvent(this, audio, track, genres, collectionId, searchHistory));
            return Optional.of(track);
        } catch (final ExecutionException ex) {
            publisher.publishEvent(new VkDownloadFailEvent(this, (DownloadException) ex.getCause(), searchHistory));
            return Optional.empty();
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
