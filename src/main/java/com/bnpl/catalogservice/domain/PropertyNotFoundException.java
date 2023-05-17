package com.bnpl.catalogservice.domain;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException(Long id) {
        super("A property with ID " + id + " not found.");
    }
}
