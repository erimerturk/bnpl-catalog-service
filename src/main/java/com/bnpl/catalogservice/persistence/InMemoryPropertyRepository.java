package com.bnpl.catalogservice.persistence;

import com.bnpl.catalogservice.domain.Property;
import com.bnpl.catalogservice.domain.PropertyRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPropertyRepository implements PropertyRepository {

    private static final Map<Long, Property> properties = new ConcurrentHashMap<>();

    @Override
    public Iterable<Property> findAll() {
        return properties.values();
    }

    @Override
    public Optional<Property> findById(Long id) {
        return existsById(id) ? Optional.of(properties.get(id)) : Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return properties.containsKey(id);
    }

    @Override
    public Property save(Property property) {
        properties.put(property.id(), property);
        return property;
    }

    @Override
    public void deleteById(Long id) {
        properties.remove(id);
    }
}
