package com.bnpl.catalogservice.demo;

import com.bnpl.catalogservice.domain.Property;
import com.bnpl.catalogservice.domain.PropertyRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="bnpl.testdata.enabled", havingValue="true")
public class PropertyDataLoader {
    public PropertyDataLoader(PropertyRepository repository) {
        this.repository = repository;
    }

    private final PropertyRepository repository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData(){

        var property1 = new Property(1l, "Title 1", "seller", 9.90);
        var property2 = new Property(2l, "Title 2", "seller", 29.90);
        var property3 = new Property(3l, "Title 3", "seller2", 59.90);

        repository.save(property1);
        repository.save(property2);
        repository.save(property3);

    }
}
