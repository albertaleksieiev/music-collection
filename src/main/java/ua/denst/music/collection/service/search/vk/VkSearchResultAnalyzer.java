package ua.denst.music.collection.service.search.vk;

import org.springframework.stereotype.Component;
import ua.denst.music.collection.domain.entity.VkAudio;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class VkSearchResultAnalyzer {
    public List<VkAudio> filterIncorrectResults(final List<VkAudio> audios, final String authors, final String title) {
        List<VkAudio> result = checkTitleAndArtistConforms(audios, authors, title);
        result = checkArtistConforms(result, authors);
        return checkTitleConforms(result, title);
    }

    private List<VkAudio> checkTitleAndArtistConforms(final List<VkAudio> audios, final String authors, final String title) {
        final Predicate<VkAudio> predicate = vkAudio -> (authors.equalsIgnoreCase(vkAudio.getArtist()) && title.equalsIgnoreCase(vkAudio.getTitle()));

        return filterByPredicateIfExists(audios, predicate);
    }

    private List<VkAudio> checkArtistConforms(final List<VkAudio> audios, final String authors) {
        final Predicate<VkAudio> artistConforms = vkAudio -> (authors.equalsIgnoreCase(vkAudio.getArtist()) ||
                vkAudio.getArtist().toLowerCase().contains(authors.toLowerCase()));

        return filterByPredicateIfExists(audios, artistConforms);
    }

    private List<VkAudio> checkTitleConforms(final List<VkAudio> audios, final String title) {
        final Predicate<VkAudio> titleConforms = vkAudio -> (title.equalsIgnoreCase(vkAudio.getTitle()) ||
                vkAudio.getTitle().toLowerCase().contains(title.toLowerCase()));

        return filterByPredicateIfExists(audios, titleConforms);
    }

    private List<VkAudio> filterByPredicateIfExists(final List<VkAudio> audios, final Predicate<VkAudio> predicate) {
        final boolean hasConformation = audios.stream().anyMatch(predicate);

        if (hasConformation) {
            return audios.stream().filter(predicate).collect(Collectors.toList());
        } else {
            return audios;
        }
    }
}
