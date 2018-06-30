package ua.denst.music.collection.service.search.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.denst.music.collection.domain.SearchStatus;
import ua.denst.music.collection.domain.dto.request.ArtistTitleDto;
import ua.denst.music.collection.domain.dto.request.SearchRequestDto;
import ua.denst.music.collection.domain.dto.request.TrackListParsingResultDto;
import ua.denst.music.collection.domain.dto.request.TrackListSearchRequestDto;
import ua.denst.music.collection.domain.dto.response.SearchAndDownloadResponseDto;
import ua.denst.music.collection.domain.entity.SearchHistory;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.domain.entity.TrackListPattern;
import ua.denst.music.collection.exception.ParsingException;
import ua.denst.music.collection.repository.TrackListPatternRepository;
import ua.denst.music.collection.service.search.SearchHistoryService;
import ua.denst.music.collection.service.search.SearchServiceFacade;
import ua.denst.music.collection.service.search.TrackListSearchService;
import ua.denst.music.collection.util.TrackListParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Transactional
@Slf4j
public class TrackListSearchServiceImpl implements TrackListSearchService {

    TrackListPatternRepository patternRepository;
    SearchServiceFacade searchServiceFacade;
    SearchHistoryService searchHistoryService;

    @Override
    public SearchAndDownloadResponseDto searchAndDownloadTrackList(final TrackListSearchRequestDto requestDto) {
        final TrackListPattern pattern = getTrackListPattern(requestDto);

        final TrackListParsingResultDto parsingResult =
                TrackListParser.parseTrackList(requestDto.getTrackList(), pattern.getValue(), requestDto.getSeparator());

        final List<Track> downloaded = new ArrayList<>();

        final List<String> notParsed = parsingResult.getNotParsed();
        notParsed.forEach(titleAuthor -> {
            final SearchHistory searchHistory = new SearchHistory();
            searchHistory.setSearchStatus(SearchStatus.FAIL);
            searchHistory.setTitle(titleAuthor);
            searchHistoryService.save(searchHistory);
        });

        parsingResult.getArtistTitles().forEach(artistTitleDto -> {
            final SearchRequestDto searchRequestDto = createSearchRequestDto(artistTitleDto, requestDto.getGenres(), requestDto.getCollectionId());
            final Optional<Track> track = searchServiceFacade.searchAndDownload(searchRequestDto);

            if (track.isPresent()) {
                downloaded.add(track.get());
            } else {
                final String artistName = artistTitleDto.getArtist() + " - " + artistTitleDto.getTitle();
                log.info("Failed search for {}", artistName);
                notParsed.add(artistName);
            }
        });

        return createResponseDto(notParsed, downloaded);
    }

    private TrackListPattern getTrackListPattern(final TrackListSearchRequestDto requestDto) {
        final TrackListPattern pattern = patternRepository.findOne(requestDto.getPatternId());

        if (pattern == null) {
            throw new ParsingException("Can't find pattern by id " + requestDto.getPatternId());
        }
        return pattern;
    }

    @Override
    public void searchAndDownloadTrackListAsync(final TrackListSearchRequestDto requestDto) {
        final TrackListPattern pattern = getTrackListPattern(requestDto);

        final TrackListParsingResultDto parsingResult =
                TrackListParser.parseTrackList(requestDto.getTrackList(), pattern.getValue(), requestDto.getSeparator());

        parsingResult.getArtistTitles().forEach(artistTitleDto -> {
            final SearchRequestDto searchRequestDto = createSearchRequestDto(artistTitleDto, requestDto.getGenres(), requestDto.getCollectionId());
            searchServiceFacade.searchAndDownloadAsync(searchRequestDto);
        });
    }

    private SearchRequestDto createSearchRequestDto(final ArtistTitleDto artistTitleDto, final Set<String> genres, final Long collectionId) {
        final SearchRequestDto searchRequestDto = new SearchRequestDto();

        searchRequestDto.setTitle(artistTitleDto.getTitle());
        searchRequestDto.setArtists(artistTitleDto.getArtist());
        searchRequestDto.setGenres(genres);
        searchRequestDto.setCollectionId(collectionId);

        return searchRequestDto;
    }

    private SearchAndDownloadResponseDto createResponseDto(final List<String> notParsed, final List<Track> downloaded) {
        final SearchAndDownloadResponseDto result = new SearchAndDownloadResponseDto();
        result.setFound(downloaded);
        result.setNotFound(notParsed);
        return result;
    }
}
