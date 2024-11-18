package com.example.carsharingapp.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carsharingapp.dto.CarDto;
import com.example.carsharingapp.dto.CreateCarRequestDto;
import com.example.carsharingapp.dto.InventoryDto;
import com.example.carsharingapp.exception.EntityNotFoundException;
import com.example.carsharingapp.model.enums.CarType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(
        scripts = {
                "classpath:database/cars/add-cars.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = {"classpath:database/cars/clear-cars.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class CarControllerTest {

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
    @DisplayName("Create a new car")
    void createCar_ValidRequest_Success() throws Exception {
        // Given
        CreateCarRequestDto requestDto = new CreateCarRequestDto();
        requestDto.setModel("Tesla Model S");
        requestDto.setBrand("Tesla");
        requestDto.setType(CarType.SEDAN);
        requestDto.setDailyFee(BigDecimal.valueOf(150));
        requestDto.setInventory(5);

        CarDto expected = new CarDto();
        expected.setModel("Tesla Model S");
        expected.setBrand("Tesla");
        expected.setType(CarType.SEDAN);
        expected.setDailyFee(BigDecimal.valueOf(150));
        expected.setInventory(5);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(post("/cars")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CarDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDto.class);

        checkAssertions(expected, actual);
    }

    private void checkAssertions(CarDto expected, CarDto actual) {
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected.getBrand(), actual.getBrand());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getDailyFee(), actual.getDailyFee());
        assertEquals(expected.getInventory(), actual.getInventory());
    }

    @WithMockUser
    @Test
    @DisplayName("Get car by ID")
    void getCarById_ValidId_Success() throws Exception {
        // Given
        long carId = 1L;

        // When
        MvcResult result = mockMvc.perform(get("/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CarDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDto.class);
        assertNotNull(response);
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @DisplayName("Update car details")
    void updateCar_ValidRequest_Success() throws Exception {
        // Given
        long carId = 1L;
        CreateCarRequestDto requestDto = new CreateCarRequestDto();
        requestDto.setModel("Updated Model");
        requestDto.setBrand("Updated Brand");
        requestDto.setType(CarType.SEDAN);
        requestDto.setDailyFee(BigDecimal.valueOf(200.00));
        requestDto.setInventory(10);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult putResult = mockMvc.perform(put("/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CarDto actual = objectMapper.readValue(
                putResult.getResponse().getContentAsString(), CarDto.class);
        assertNotNull(actual);

        MvcResult getResult = mockMvc.perform(get("/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CarDto expected = objectMapper.readValue(
                getResult.getResponse().getContentAsString(), CarDto.class);
        assertEquals(actual.getBrand(), expected.getBrand());
        assertEquals(actual.getModel(), expected.getModel());
        assertEquals(actual.getType(), expected.getType());
        assertEquals(expected.getDailyFee().setScale(2, RoundingMode.HALF_UP),
                actual.getDailyFee().setScale(2, RoundingMode.HALF_UP));
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @DisplayName("Delete car by ID")
    void deleteCarById_ValidId_Success() throws Exception {
        // Given
        long carId = 1L;

        // When
        mockMvc.perform(delete("/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get("/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof EntityNotFoundException,
                        "Expected EntityNotFoundException"));
    }

    @WithMockUser
    @Test
    @DisplayName("Get all cars")
    void getAll_GivenCarsInStore_ShouldReturnAllCars() throws Exception {
        // Given
        CarDto car1 = new CarDto();
        car1.setId(1L);
        car1.setBrand("Audi");
        car1.setModel("Q7");
        car1.setType(CarType.SEDAN);
        car1.setInventory(10);
        car1.setDailyFee(BigDecimal.valueOf(50));
        CarDto car2 = new CarDto();
        car2.setId(1L);
        car2.setBrand("Tesla");
        car2.setModel("S");
        car2.setType(CarType.SEDAN);
        car2.setInventory(15);
        car2.setDailyFee(BigDecimal.valueOf(40));
        List<CarDto> expected = List.of(car1, car2);

        // When
        MvcResult result = mockMvc.perform(get("/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CarDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDto[].class);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.length);
        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @DisplayName("Update car inventory")
    void changeInventory_ValidRequest_Success() throws Exception {
        // Given
        long carId = 1L;
        InventoryDto inventoryDto = new InventoryDto(20);

        String jsonRequest = objectMapper.writeValueAsString(inventoryDto);

        // When
        MvcResult result = mockMvc.perform(patch("/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CarDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDto.class);
        assertNotNull(response);
    }
}
