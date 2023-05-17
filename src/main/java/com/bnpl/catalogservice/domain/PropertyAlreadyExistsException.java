package com.bnpl.catalogservice.domain;

public class PropertyAlreadyExistsException extends RuntimeException {
    public PropertyAlreadyExistsException(Long id) {
        super("A property with ID " + id + " already exists.");
    }
}
