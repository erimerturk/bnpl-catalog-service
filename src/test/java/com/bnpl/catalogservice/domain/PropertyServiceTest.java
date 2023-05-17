package com.bnpl.catalogservice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository repository;

    @InjectMocks
    private PropertyService service;

    @Test
    void whenPropertyToCreateAlreadyExistsThenThrows() {
        var id = 11l;
        var toCreate = new Property(id, "Title", "seller", 1119.90);
        when(repository.existsById(id)).thenReturn(true);
        assertThatThrownBy(() -> service.addPropertyToCatalog(toCreate))
                .isInstanceOf(PropertyAlreadyExistsException.class)
                .hasMessage("A property with ID " + id + " already exists.");
    }

    @Test
    void whenPropertyToReadDoesNotExistThenThrows() {
        var id = 11l;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.viewPropertyDetails(id))
                .isInstanceOf(PropertyNotFoundException.class)
                .hasMessage("A property with ID " + id + " not found.");
    }

}