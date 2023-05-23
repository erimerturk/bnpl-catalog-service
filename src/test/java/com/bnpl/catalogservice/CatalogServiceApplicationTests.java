package com.bnpl.catalogservice;

import com.bnpl.catalogservice.domain.Property;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Testcontainers
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    // Customer
    private static KeycloakToken bjornTokens;
    // Customer and employee
    private static KeycloakToken isabelleTokens;

    @Container
    private static final KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:19.0")
            .withRealmImportFile("test-realm-config.json");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realms/BNPLRealEstate");
    }

    @BeforeAll
    static void generateAccessTokens() {
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl() + "realms/BNPLRealEstate/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        isabelleTokens = authenticateWith("isabelle", "password", webClient);
        bjornTokens = authenticateWith("bjorn", "password", webClient);
    }


    @Test
    void whenGetRequestWithIdThenPropertyReturned() {
        var id = 2l;
        var toCreate = Property.of(id, "Title", "seller", 9.90);
        Property expected = webTestClient
                .post()
                .uri("/properties")
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .bodyValue(toCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Property.class).value(property -> assertThat(property).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/properties/" + id)
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
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
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
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
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .bodyValue(toCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Property.class).value(property -> assertThat(property).isNotNull())
                .returnResult().getResponseBody();
        var toUpdate = Property.of(created.id(), created.title(), created.seller(), 7.95);

        webTestClient
                .put()
                .uri("/properties/" + id)
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
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
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .bodyValue(toCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .delete()
                .uri("/properties/" + id)
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/properties/" + id)
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(errorMessage ->
                        assertThat(errorMessage).isEqualTo("A property with ID " + id + " not found.")
                );
    }

    private static KeycloakToken authenticateWith(String username, String password, WebClient webClient) {
        return webClient
                .post()
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "catalog-test")
                        .with("username", username)
                        .with("password", password)
                )
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }

    private record KeycloakToken(String accessToken) {

        @JsonCreator
        private KeycloakToken(@JsonProperty("access_token") final String accessToken) {
            this.accessToken = accessToken;
        }

    }

}