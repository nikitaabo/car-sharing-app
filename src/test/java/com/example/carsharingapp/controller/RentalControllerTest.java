package com.example.carsharingapp.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carsharingapp.dto.RentalDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(
        scripts = {
                "classpath:database/cars/add-cars.sql",
                "classpath:database/users/add-users.sql",
                "classpath:database/rentals/add-rentals.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = {
                "classpath:database/rentals/clear-rentals.sql",
                "classpath:database/cars/clear-cars.sql",
                "classpath:database/users/clear-users.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class RentalControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @DisplayName("Get rental by ID")
    void getRentalById_ValidId_Success() throws Exception {
        // Given
        long rentalId = 1L;

        // When
        MvcResult result = mockMvc.perform(get("/rentals/{id}", rentalId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        RentalDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(), RentalDto.class);
        assertNotNull(response);
    }

    @WithUserDetails("user@gmail.com")
    @Test
    @DisplayName("Get all rentals")
    void getAllRentals_GivenRentalsInStore_ShouldReturnAllRentals() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/rentals?is_active=true")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        RentalDto[] rentals = objectMapper.readValue(
                result.getResponse().getContentAsString(), RentalDto[].class);
        assertNotNull(rentals);
        assertEquals(rentals.length, 2);
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @DisplayName("Return rental by ID")
    void returnRental_ValidId_Success() throws Exception {
        // Given
        long rentalId = 1L;

        // When
        MvcResult result = mockMvc.perform(post("/rentals/{id}/return", rentalId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        RentalDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(), RentalDto.class);
        assertNotNull(response);
        assertEquals(response.getId().longValue(), rentalId);
        assertFalse(response.getIsActive());
    }
}
