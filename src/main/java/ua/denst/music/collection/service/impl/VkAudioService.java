package ua.denst.music.collection.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.denst.music.collection.domain.entity.VkAudio;
import ua.denst.music.collection.domain.event.DownloadStatusEvent;
import ua.denst.music.collection.repository.AudioRepository;
import ua.denst.music.collection.service.AudioService;

@Transactional
@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class VkAudioService implements AudioService {

    AudioRepository audioRepository;

    @Override
    public void save(final VkAudio audio) {
        audioRepository.save(audio);
    }

    @Override
    public void onDownloadStatusEvent(final DownloadStatusEvent event) {
        final VkAudio audio = event.getAudio();
        final VkAudio entity = audioRepository.findOne(audio.getId());
        entity.setStatus(event.getStatus());
        save(entity);
    }

}
