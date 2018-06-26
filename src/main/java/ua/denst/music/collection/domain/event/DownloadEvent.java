package ua.denst.music.collection.domain.event;

import org.springframework.context.ApplicationEvent;

public abstract class DownloadEvent extends ApplicationEvent {
    DownloadEvent(Object source) {
        super(source);
    }
}
