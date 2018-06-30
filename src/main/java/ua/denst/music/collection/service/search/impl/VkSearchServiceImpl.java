package ua.denst.music.collection.service.search.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.springframework.stereotype.Service;
import ua.denst.music.collection.client.VkClient;
import ua.denst.music.collection.domain.dto.vk.request.VkSearchAudioRequestDto;
import ua.denst.music.collection.domain.dto.vk.response.VkSearchResponseDto;
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.service.TrackService;
import ua.denst.music.collection.service.search.SearchHistoryService;
import ua.denst.music.collection.service.search.VkAudioService;
import ua.denst.music.collection.service.search.VkDownloadService;
import ua.denst.music.collection.service.search.VkSearchService;
import ua.denst.music.collection.service.search.vk.VkAudioLoader;
import ua.denst.music.collection.service.search.vk.VkSearchResultAnalyzer;
import ua.denst.music.collection.service.search.vk.VkSearchResultProcessor;
import ua.denst.music.collection.util.BitRateCalculator;
import ua.denst.music.collection.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ua.denst.music.collection.util.Constants.USER_ID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@AllArgsConstructor
public class VkSearchServiceImpl implements VkSearchService {
    static String AUTHOR_TITLE_DELIMITER = " - ";
    static String CONTENT_RANGE_HEADER = "content-range";

    VkClient vkClient;
    VkSearchResultAnalyzer vkSearchResultAnalyzer;
    VkSearchResultProcessor vkSearchResultProcessor;
    VkAudioLoader vkAudioLoader;

    TrackService trackService;
    SearchHistoryService searchHistoryService;
    VkDownloadService vkDownloadService;
    VkAudioService vkAudioService;

    @Override
    public Optional<Track> searchAndDownload(final String authors, final String title, final Set<String> genres,
                                             final Long collectionId, final SearchHistory searchHistory) {

        final Optional<VkAudio> vkAudio = search(authors, title);

        if (vkAudio.isPresent()) {
            final VkAudio audio = vkAudio.get();
            final Track fromDb = trackService.findByArtistAndTitle(audio.getArtist(), audio.getTitle());
            if (fromDb != null) {
                log.debug("Found track in db, not perform searching.");
                searchHistoryService.saveSuccess(searchHistory, fromDb, true);
                return Optional.of(fromDb);
            }
            final Optional<Track> downloaded = saveVkAudioAndDownloadTrack(audio, genres, collectionId, searchHistory);
            if (downloaded.isPresent()) {
                final Track saved = trackService.save(downloaded.get(), genres, collectionId);
                return Optional.of(saved);
            }
        }
        return Optional.empty();
    }

    @Override
    public void searchAndDownloadAsync(final String authors, final String title, final Set<String> genres,
                                       final Long collectionId, final SearchHistory searchHistory) {

        final Optional<VkAudio> vkAudio = search(authors, title);

        if (vkAudio.isPresent()) {
            final VkAudio audio = vkAudio.get();

            final Track fromDb = trackService.findByArtistAndTitle(audio.getArtist(), audio.getTitle());
            if (fromDb != null) {
                log.debug("Found track in db, not perform searching.");
                searchHistoryService.saveSuccess(searchHistory, fromDb, true);
            } else {
                vkAudioService.save(audio);
                vkDownloadService.downloadAsync(audio, genres, collectionId, searchHistory);
            }
        } else {
            searchHistoryService.saveFail(searchHistory);
        }
    }

    @SneakyThrows
    private Optional<VkAudio> search(final String authors, final String title) {
        final String searchQuery = authors + AUTHOR_TITLE_DELIMITER + title;
        final List<VkSearchResponseDto> searchResults = getSearchResult(searchQuery);

        final VkSearchAudioRequestDto vkSearchAudioRequestDto = vkSearchResultProcessor.processSearchResults(searchResults);

        List<VkAudio> audios = vkAudioLoader.load(vkSearchAudioRequestDto);

        if (audios.isEmpty()) {
            log.warn("Empty search result");
            return Optional.empty();
        } else {
            audios = vkSearchResultAnalyzer.filterIncorrectResults(audios, authors, title);

            sortByBitRateAndDuration(audios);

            log.info("Success search by query \"{}\"", searchQuery);
            return Optional.of(audios.get(audios.size() - 1));
        }
    }

    private List<VkSearchResponseDto> getSearchResult(final String searchQuery) {
        final List<VkSearchResponseDto> result = new ArrayList<>();

        VkSearchResponseDto vkSearchResponseDto;
        int offset = 0;
        do {
            final Connection.Response response = vkClient.search(USER_ID, searchQuery, offset);
            vkSearchResponseDto = JsonUtils.responseToObject(response.body(), VkSearchResponseDto.class);

            offset = vkSearchResponseDto.getNextOffset();

            result.add(vkSearchResponseDto);
        } while (vkSearchResponseDto.getHasMore());

        return result;
    }

    private void sortByBitRateAndDuration(final List<VkAudio> list) {
        calculateBitRatesAndSize(list);

        list.sort((o1, o2) -> {
            final int compareBitRates = o1.getBitRate().compareTo(o2.getBitRate());
            if (compareBitRates != 0) {
                return compareBitRates;
            } else {
                return o1.getDuration().compareTo(o2.getDuration());
            }
        });
    }

    @SneakyThrows
    private void calculateBitRatesAndSize(final List<VkAudio> list) {
        for (final VkAudio audio : list) {
            final Connection.Response response = vkClient.callUrl(audio.getUrl());

            final String sizeHeader = response.header(CONTENT_RANGE_HEADER);
            final Long sizeLong = Long.valueOf(sizeHeader.substring(10));
            final Double sizeMb = sizeLong / 1024 / 1024.;

            final Short bitRate = BitRateCalculator.calculateBitRate(audio.getDuration(), sizeLong);
            audio.setBitRate(bitRate);
            audio.setSizeMb(sizeMb);
        }
    }

    private Optional<Track> saveVkAudioAndDownloadTrack(final VkAudio audio, final Set<String> genres,
                                                        final Long collectionId, final SearchHistory searchHistory) {
        vkAudioService.save(audio);
        return vkDownloadService.download(audio, genres, collectionId, searchHistory);
    }
}
