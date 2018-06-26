package ua.denst.music.collection.service;

import lombok.NonNull;
import ua.denst.music.collection.property.Properties;

public interface PropertyService {

    <T extends Properties> T set(@NonNull T properties);

    <T extends Properties> T get(Class<T> clazz);

    <T extends Properties> void remove(Class<T> clazz);

}
