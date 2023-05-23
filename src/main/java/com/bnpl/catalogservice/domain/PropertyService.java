package com.bnpl.catalogservice.domain;

import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    private final PropertyRepository repository;

    public PropertyService(PropertyRepository repository) {
        this.repository = repository;
    }

    public Iterable<Property> viewPropertyList() {
        return repository.findAll();
    }

    public Property viewPropertyDetails(Long id) {
        return repository.findById(id).orElseThrow(() -> new PropertyNotFoundException(id));
    }

    public Property addPropertyToCatalog(Property property) {
        if (repository.existsById(property.id())) {
            throw new PropertyAlreadyExistsException(property.id());
        }
        return repository.save(property);
    }

    public void removePropertyFromCatalog(Long id) {
        repository.deleteById(id);
    }

    public Property editPropertyDetails(Long id, Property property) {
        return repository.findById(id)
                .map(existing -> {
                    var toUpdate = new Property(
                            existing.id(),
                            property.title(),
                            property.seller(),
                            property.price(),
                            existing.version(),
                            existing.createdDate(),
                            existing.lastModifiedDate(),
                            existing.createdBy(),
                            existing.lastModifiedBy(),
                            property.category());
                    return repository.save(toUpdate);
                })
                .orElseGet(() -> addPropertyToCatalog(property));
    }
}
