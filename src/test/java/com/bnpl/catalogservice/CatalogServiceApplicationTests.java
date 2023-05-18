package com.bnpl.catalogservice;

import com.bnpl.catalogservice.domain.Property;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenGetRequestWithIdThenPropertyReturned() {
        var id = 2l;
        var toCreate = Property.of(id, "Title", "seller", 9.90);
        Property expected = webTestClient
                .post()
                .uri("/properties")
                .bodyValue(toCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Property.class).value(property -> assertThat(property).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/properties/" + id)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Property.class).value(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.id()).isEqualTo(expected.id());
                });
    }

    @Test
    void whenPostRequestThenPropertyCreated() {

        var id = 3l;
        var expected = Property.of(id, "Title", "seller", 9.90);

        webTestClient
                .post()
                .uri("/properties")
                .bodyValue(expected)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Property.class).value(property -> {
                    assertThat(property).isNotNull();
                    assertThat(property.id()).isEqualTo(expected.id());
                });
    }

    @Test
    void whenPutRequestThenPropertyUpdated() {
        var id = 1l;
        var toCreate = Property.of(id, "Title", "seller", 9.90);
        Property created = webTestClient
                .post()
                .uri("/properties")
                .bodyValue(toCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Property.class).value(property -> assertThat(property).isNotNull())
                .returnResult().getResponseBody();
        var toUpdate = Property.of(created.id(), created.title(), created.seller(), 7.95);

        webTestClient
                .put()
                .uri("/properties/" + id)
                .bodyValue(toUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Property.class).value(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.price()).isEqualTo(toUpdate.price());
                });
    }

    @Test
    void whenDeleteRequestThenPropertyDeleted() {
        var id = 4l;
        var toCreate = Property.of(id, "Title", "seller", 9.90);
        webTestClient
                .post()
                .uri("/properties")
                .bodyValue(toCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .delete()
                .uri("/properties/" + id)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/properties/" + id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(errorMessage ->
                        assertThat(errorMessage).isEqualTo("A property with ID " + id + " not found.")
                );
    }

}