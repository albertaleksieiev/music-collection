package ua.denst.music.collection.service.search.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.domain.event.vk.VkDownloadStatusEvent;
import ua.denst.music.collection.repository.VkAudioRepository;
import ua.denst.music.collection.service.search.VkAudioService;

@Transactional
@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class VkAudioServiceImpl implements VkAudioService {

    VkAudioRepository vkAudioRepository;

    @Override
    public void save(final VkAudio audio) {
        vkAudioRepository.save(audio);
    }

    @Override
    public void onDownloadStatusEvent(final VkDownloadStatusEvent event) {
        final VkAudio audio = event.getAudio();
        final VkAudio entity = vkAudioRepository.findOne(audio.getId());
        entity.setStatus(event.getStatus());
        save(entity);
    }

}
