package com.bnpl.catalogservice.domain;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends CrudRepository<Property, Long> {

    Optional<Property> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}
