package com.bnpl.catalogservice.demo;

import com.bnpl.catalogservice.domain.Property;
import com.bnpl.catalogservice.domain.PropertyRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name="bnpl.testdata.enabled", havingValue="true")
public class PropertyDataLoader {
    public PropertyDataLoader(PropertyRepository repository) {
        this.repository = repository;
    }

    private final PropertyRepository repository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData(){
        repository.deleteAll();

        var property1 = Property.of(1l, "Title 1", "seller", 9.90);
        var property2 = Property.of(2l, "Title 2", "seller", 29.90);
        var property3 = Property.of(3l, "Title 3", "seller2", 59.90);

        repository.saveAll(List.of(property1, property2, property3));

    }
}
