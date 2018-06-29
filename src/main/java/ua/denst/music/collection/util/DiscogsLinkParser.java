package ua.denst.music.collection.util;

import ua.denst.music.collection.exception.InvalidDiscogsLinkException;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.denst.music.collection.service.impl.DiscogsServiceImpl.DATABASE_DETERMINER;
import static ua.denst.music.collection.service.impl.DiscogsServiceImpl.MARKETPLACE_DETERMINER;

public class DiscogsLinkParser {

    public static Long parseAsRelease(final String link) {
        return parse(link, DATABASE_DETERMINER);
    }

    public static Long parseAsMarketplaceItem(final String link) {
        return parse(link, MARKETPLACE_DETERMINER);
    }

    private static Long parse(final String link, final String determiner) {
        final int indexOfRelease = link.indexOf(determiner);

        if (indexOfRelease > 0) {
            final String id = link.substring(indexOfRelease + determiner.length() + 1);
            return getNumber(id);
        } else {
            throw new InvalidDiscogsLinkException(MessageFormat.format("Can't parse link \"{}\". Link should contain \"{}\" word", link, determiner));
        }
    }

    private static Long getNumber(final String str) {
        final Matcher matcher = Pattern.compile("\\d+").matcher(str);
        matcher.find();
        return Long.valueOf(matcher.group());
    }
}
