package com.bnpl.catalogservice.domain;

import com.bnpl.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
class PropertyRepositoryJdbcTests {

    @Autowired
    private PropertyRepository repository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findAllProperties() {
        var property1 = Property.of(234567l, "Title", "seller", 12.90);
        var property2 = Property.of(8972139l, "Another Title", "seller", 12.90);
        jdbcAggregateTemplate.insert(property2);
        jdbcAggregateTemplate.insert(property1);

        Iterable<Property> actualProperties = repository.findAll();

        assertThat(StreamSupport.stream(actualProperties.spliterator(), true)
                .filter(property -> property.id().equals(property1.id()) || property.id().equals(property2.id()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void findPropertyByIdWhenExisting() {
        var id = 999l;
        var property = Property.of(id, "Title", "seller", 12.90);
        jdbcAggregateTemplate.insert(property);

        Optional<Property> actual = repository.findById(id);

        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(property.id());
    }

    @Test
    void findPropertyByIdWhenNotExisting() {
        Optional<Property> actual = repository.findById(4444l);
        assertThat(actual).isEmpty();
    }

    @Test
    void existsByIdWhenExisting() {
        var id = 33l;
        var toCreate = Property.of(id, "Title", "seller", 12.90);
        jdbcAggregateTemplate.insert(toCreate);

        boolean existing = repository.existsById(id);

        assertThat(existing).isTrue();
    }


    @Test
    void deleteById() {
        var id = 555l;
        var toCreate = Property.of(id, "Title", "seller", 12.90);
        var persisted = jdbcAggregateTemplate.insert(toCreate);

        repository.deleteById(id);

        assertThat(jdbcAggregateTemplate.findById(persisted.id(), Property.class)).isNull();
    }

    @Test
    @WithMockUser("dddd")
    void whenCreatePropertyAuthenticatedThenAuditMetadata() {
        var id = 555l;
        var toCreate = Property.of(id, "Title", "seller", 12.90);
        var persisted = jdbcAggregateTemplate.insert(toCreate);

        assertThat(persisted.createdBy()).isEqualTo("dddd");
        assertThat(persisted.lastModifiedBy()).isEqualTo("dddd");
    }

    @Test
    void whenCreatePropertyNotAuthenticatedThenNoAuditMetadata() {
        var id = 555l;
        var toCreate = Property.of(id, "Title", "seller", 12.90);
        var persisted = jdbcAggregateTemplate.insert(toCreate);

        assertThat(persisted.createdBy()).isNull();
        assertThat(persisted.lastModifiedBy()).isNull();
    }

}
