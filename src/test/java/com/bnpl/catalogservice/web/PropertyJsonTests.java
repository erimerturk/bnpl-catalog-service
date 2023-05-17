package com.bnpl.catalogservice.web;

import com.bnpl.catalogservice.domain.Property;
import com.bnpl.catalogservice.domain.PropertyNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class PropertyJsonTests {

    @Autowired
    private JacksonTester<Property> json;

    @Test
    void testSerialize() throws Exception {
        var property = new Property(1l, "Title", "seller", 9.90);
        var jsonContent = json.write(property);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .isEqualTo(property.id().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(property.title());
        assertThat(jsonContent).extractingJsonPathStringValue("@.seller")
                .isEqualTo(property.seller());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(property.price());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                    "id": 1,
                    "title": "Title",
                    "seller": "seller",
                    "price": 9.90
                }
                """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new Property(1L, "Title", "seller", 9.90));
    }

}
