package com.bnpl.catalogservice.web;

import com.bnpl.catalogservice.domain.Property;
import com.bnpl.catalogservice.domain.PropertyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("properties")
public class PropertyController {
    private static final Logger log = LoggerFactory.getLogger(PropertyController.class);

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public Iterable<Property> get() {
        log.info("Fetching list of properties int the catalog");
        return propertyService.viewPropertyList();
    }

    @GetMapping("{id}")
    public Property getByIsbn(@PathVariable Long id) {
        return propertyService.viewPropertyDetails(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Property post(@Valid @RequestBody Property property) {
        return propertyService.addPropertyToCatalog(property);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        propertyService.removePropertyFromCatalog(id);
    }

    @PutMapping("{id}")
    public Property put(@PathVariable Long id, @Valid @RequestBody Property property) {
        return propertyService.editPropertyDetails(id, property);
    }


}
