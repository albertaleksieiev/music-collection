package ua.denst.music.collection.service.search;

import ua.denst.music.collection.domain.entity.TrackListPattern;

public interface TrackListPatternService {
    TrackListPattern create(String pattern, String example);
}
