package ua.denst.music.collection.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ua.denst.music.collection.client.DiscogsClient;
import ua.denst.music.collection.domain.dto.request.SearchRequestDto;
import ua.denst.music.collection.domain.dto.response.SearchAndDownloadResponseDto;
import ua.denst.music.collection.domain.dto.response.DiscogsTrackListResponseDto;
import ua.denst.music.collection.domain.dto.response.discogs.DiscogsArtistDto;
import ua.denst.music.collection.domain.dto.response.discogs.DiscogsMarketPlaceListingResponseDto;
import ua.denst.music.collection.domain.dto.response.discogs.DiscogsReleaseResponseDto;
import ua.denst.music.collection.domain.dto.response.discogs.DiscogsTrackDto;
import ua.denst.music.collection.domain.entity.Track;
import ua.denst.music.collection.exception.InvalidDiscogsLinkException;
import ua.denst.music.collection.service.DiscogsService;
import ua.denst.music.collection.service.search.SearchServiceFacade;
import ua.denst.music.collection.util.JsonUtils;

import java.text.MessageFormat;
import java.util.*;

import static ua.denst.music.collection.util.DiscogsLinkParser.parseAsMarketplaceItem;
import static ua.denst.music.collection.util.DiscogsLinkParser.parseAsRelease;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DiscogsServiceImpl implements DiscogsService {
    public static final String MARKETPLACE_DETERMINER = "sell";
    public static final String DATABASE_DETERMINER = "release";

    private static final String DEFAULT_ARTISTS_DELIMITER = " & ";

    DiscogsClient discogsClient;
    SearchServiceFacade searchService;

    @Override
    public SearchAndDownloadResponseDto searchAndDownload(final String link, final Long collectionId) {
        final Long releaseId = getReleaseId(link);

        final DiscogsReleaseResponseDto release = getReleaseDto(releaseId);

        return processDiscogsRelease(release, collectionId);
    }

    @Override
    public List<DiscogsTrackListResponseDto> getTrackList(final String link) {
        final Long releaseId = getReleaseId(link);

        final DiscogsReleaseResponseDto release = getReleaseDto(releaseId);
        final String artists = concatArtists(release.getArtists());

        return toTrackListDto(release.getTracklist(), artists);
    }

    private Long getReleaseId(final String link) {
        log.debug("Processing discogs link {}", link);

        final Long releaseId;

        if (link.contains(MARKETPLACE_DETERMINER)) {
            releaseId = getReleaseIdFromMarketplaceLink(link);
        } else if (link.contains(DATABASE_DETERMINER)) {
            releaseId = parseAsRelease(link);
            log.debug("Parsed release id {}", releaseId);
        } else {
            throw new InvalidDiscogsLinkException(MessageFormat.format("Discogs link should contain one of ({},{}) substrings.", MARKETPLACE_DETERMINER, DATABASE_DETERMINER));
        }

        return releaseId;
    }

    private Long getReleaseIdFromMarketplaceLink(final String link) {
        final Long releaseId;
        final Long itemId = parseAsMarketplaceItem(link);
        log.debug("Parsed item id {}", itemId);

        final Connection.Response response = discogsClient.getMarketplaceItem(itemId);
        final DiscogsMarketPlaceListingResponseDto responseDto = JsonUtils.fromString(response.body(), DiscogsMarketPlaceListingResponseDto.class);

        releaseId = responseDto.getRelease().getId();
        log.debug("Received release id {}", releaseId);

        return releaseId;
    }

    private SearchAndDownloadResponseDto processDiscogsRelease(final DiscogsReleaseResponseDto release, final Long collectionId) {
        final Set<String> genres = new HashSet<>(release.getStyles());
        //TODO perform search if need for each artist and all combinations of artists
        final String artists = concatArtists(release.getArtists());
        log.debug("Parsed artists: {}", artists);

        final Map<String, Optional<Track>> tracks = processTrackList(release.getTracklist(), artists, genres, collectionId);

        return toDto(tracks, artists);
    }

    private DiscogsReleaseResponseDto getReleaseDto(final Long releaseId) {
        final Connection.Response response = discogsClient.getRelease(releaseId);
        return JsonUtils.fromString(response.body(), DiscogsReleaseResponseDto.class);
    }


    private String concatArtists(final List<DiscogsArtistDto> artists) {
        if (!CollectionUtils.isEmpty(artists)) {
            return StringUtil.join(artists.stream().map(DiscogsArtistDto::getName).iterator(), DEFAULT_ARTISTS_DELIMITER);
        } else {
            return "";
        }
    }

    private Map<String, Optional<Track>> processTrackList(final List<DiscogsTrackDto> trackList, final String artists,
                                                          final Set<String> genres, final Long collectionId) {
        final Map<String, Optional<Track>> result = new HashMap<>();

        if (!CollectionUtils.isEmpty(trackList)) {
            trackList.forEach(discogsTrackDto -> {
                final String title = discogsTrackDto.getTitle();

                final SearchRequestDto searchRequestDto = createSearchRequestDto(artists, title, genres, collectionId);

                final Optional<Track> track = searchService.searchAndDownload(searchRequestDto);
                result.put(title, track);
            });
        }

        return result;
    }

    private SearchRequestDto createSearchRequestDto(final String artists, final String title,
                                                    final Set<String> genres, final Long collectionId) {
        final SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setArtists(artists);
        searchRequestDto.setCollectionId(collectionId);
        searchRequestDto.setGenres(genres);
        searchRequestDto.setTitle(title);
        return searchRequestDto;
    }

    private SearchAndDownloadResponseDto toDto(final Map<String, Optional<Track>> nameToTrackMap, final String artists) {
        final SearchAndDownloadResponseDto dto = new SearchAndDownloadResponseDto();
        final List<Track> tracks = new ArrayList<>();
        final List<String> notFound = new ArrayList<>();

        nameToTrackMap.forEach((title, trackOptional) -> {
            if (trackOptional.isPresent()) {
                tracks.add(trackOptional.get());
            } else {
                notFound.add(artists + " - " + title);
            }
        });

        dto.setFound(tracks);
        dto.setNotFound(notFound);
        return dto;
    }

    private List<DiscogsTrackListResponseDto> toTrackListDto(final List<DiscogsTrackDto> trackList, final String artistsRelease) {
        final List<DiscogsTrackListResponseDto> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(trackList)) {
            trackList.forEach(trackDto -> {
                final DiscogsTrackListResponseDto dto = getTrackListDto(trackDto, artistsRelease);
                result.add(dto);
            });
        }

        return result;
    }

    private DiscogsTrackListResponseDto getTrackListDto(final DiscogsTrackDto trackDto, final String artistsRelease) {
        final DiscogsTrackListResponseDto dto = new DiscogsTrackListResponseDto();

        dto.setTitle(trackDto.getTitle());
        dto.setPosition(trackDto.getPosition());

        final String artists = !CollectionUtils.isEmpty(trackDto.getArtists()) ? concatArtists(trackDto.getArtists()) : artistsRelease;
        dto.setArtists(artists);

        return dto;
    }

}
