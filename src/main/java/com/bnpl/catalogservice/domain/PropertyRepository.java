package com.bnpl.catalogservice.domain;

import java.util.Optional;

public interface PropertyRepository {

    Iterable<Property> findAll();
    Optional<Property> findById(Long id);
    boolean existsById(Long id);
    Property save(Property property);
    void deleteById(Long id);
}
