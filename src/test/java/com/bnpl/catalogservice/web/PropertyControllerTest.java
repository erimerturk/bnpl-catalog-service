package com.bnpl.catalogservice.web;

import com.bnpl.catalogservice.config.SecurityConfig;
import com.bnpl.catalogservice.domain.Property;
import com.bnpl.catalogservice.domain.PropertyNotFoundException;
import com.bnpl.catalogservice.domain.PropertyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PropertyController.class)
@Import(SecurityConfig.class)
class PropertyControllerTest {

    private static final String ROLE_EMPLOYEE = "ROLE_employee";
    private static final String ROLE_CUSTOMER = "ROLE_customer";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PropertyService propertyService;


    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void whenGetPropertyNotExistingThenShouldReturn404() throws Exception {
        var id = 333l;
        given(propertyService.viewPropertyDetails(id)).willThrow(PropertyNotFoundException.class);
        mockMvc
                .perform(get("/properties/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetPropertyExistingAndAuthenticatedThenShouldReturn200() throws Exception {
        var id = 3333l;
        var expected = Property.of(id, "Title", "seller", 9.90);
        given(propertyService.viewPropertyDetails(id)).willReturn(expected);
        mockMvc
                .perform(get("/properties/" + id)
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutPropertyWithCustomerRoleThenShouldReturn403() throws Exception {
        var id = 3333l;
        var expected = Property.of(id, "Title", "seller", 9.90);
        given(propertyService.addPropertyToCatalog(expected)).willReturn(expected);
        mockMvc
                .perform(put("/properties/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CUSTOMER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPutBookAndNotAuthenticatedThenShouldReturn401() throws Exception {
        var id = 3333l;
        var expected = Property.of(id, "Title", "seller", 9.90);
        mockMvc
                .perform(put("/properties/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected)))
                .andExpect(status().isUnauthorized());
    }

}