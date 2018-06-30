package ua.denst.music.collection.service.search.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.denst.music.collection.domain.dto.request.TrackListParsingResultDto;
import ua.denst.music.collection.domain.entity.TrackListPattern;
import ua.denst.music.collection.exception.ParsingException;
import ua.denst.music.collection.repository.TrackListPatternRepository;
import ua.denst.music.collection.service.search.TrackListPatternService;
import ua.denst.music.collection.util.TrackListParser;

import java.text.MessageFormat;

import static ua.denst.music.collection.util.TrackListParser.ARTISTS_LABEL;
import static ua.denst.music.collection.util.TrackListParser.TITLE_LABEL;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Transactional
@Slf4j
public class TrackListPatternServiceImpl implements TrackListPatternService {

    TrackListPatternRepository repository;

    @Override
    public TrackListPattern create(final String regex, final String example) {

        if (TrackListParser.canParse(regex, example)) {
            final TrackListPattern pattern = createPattern(regex, example);

            return repository.save(pattern);
        } else {
            throw new ParsingException(MessageFormat.format("Can't parse \"{}\" by regex \"{}\". " +
                    "Pattern can't be compiled or don't have labels {} or {}", example, regex, ARTISTS_LABEL, TITLE_LABEL));
        }
    }

    private TrackListPattern createPattern(final String regex, final String example) {
        final TrackListPattern pattern = new TrackListPattern();

        pattern.setExample(example);
        pattern.setValue(regex);
        return pattern;
    }
}
