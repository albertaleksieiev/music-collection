package ua.denst.music.collection.service.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.denst.music.collection.domain.entity.Property;
import ua.denst.music.collection.property.DownloaderProperties;
import ua.denst.music.collection.property.Properties;
import ua.denst.music.collection.repository.PropertyRepository;
import ua.denst.music.collection.service.PropertyService;
import ua.denst.music.collection.util.JsonUtils;

import javax.annotation.PostConstruct;

@Transactional
@Service
@DependsOn("jsonUtils")
@CacheConfig(cacheNames = "properties")
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyServiceImpl(@NonNull PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @PostConstruct
    private void init() {
        if (get(DownloaderProperties.class) == null) {
            set(new DownloaderProperties());
        }
    }

    @CachePut(key = "#properties.class")
    @Override
    public <T extends Properties> T set(T properties) {
        Property entity = null;
        entity = new Property(properties.getClass().getSimpleName(), JsonUtils.toString(properties));
        propertyRepository.save(entity);
        return properties;
    }

    @Cacheable
    @Override
    public <T extends Properties> T get(Class<T> clazz) {
        Property entity = propertyRepository.findOne(clazz.getSimpleName());
        if (entity != null) {
            return JsonUtils.fromString(entity.getJson(), clazz);
        }

        return null;
    }

    @CacheEvict
    @Override
    public <T extends Properties> void remove(Class<T> clazz) {
        String key = clazz.getSimpleName();
        if (propertyRepository.exists(key)) {
            propertyRepository.delete(key);
        }
    }

}
