package ua.denst.music.collection.property;

import lombok.Data;

@Data
public class DownloaderProperties implements Properties {

    private int poolSize = 5;

    private long autoSyncDelay = 60;
    private boolean autoSync = false;

}
