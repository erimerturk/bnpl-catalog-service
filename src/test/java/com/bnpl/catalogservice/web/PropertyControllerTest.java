package com.bnpl.catalogservice.web;

import com.bnpl.catalogservice.domain.PropertyNotFoundException;
import com.bnpl.catalogservice.domain.PropertyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PropertyController.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @Test
    void whenGetPropertyNotExistingThenShouldReturn404() throws Exception {
        var id = 333l;
        given(propertyService.viewPropertyDetails(id)).willThrow(PropertyNotFoundException.class);
        mockMvc
                .perform(get("/properties/" + id))
                .andExpect(status().isNotFound());
    }

}