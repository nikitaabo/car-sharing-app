package com.example.carsharingapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carsharingapp.dto.UpdateUserProfileRequestDto;
import com.example.carsharingapp.dto.UpdateUserRoleRequestDto;
import com.example.carsharingapp.dto.UserDto;
import com.example.carsharingapp.model.User;
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
                "classpath:database/users/add-users.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = {"classpath:database/users/clear-users.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class UserControllerTest {

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @DisplayName("Update user role")
    void updateUserRole_ValidRequest_Success() throws Exception {
        // Given
        long userId = 1L;
        UpdateUserRoleRequestDto roleRequestDto = new UpdateUserRoleRequestDto();
        roleRequestDto.setRole(User.UserRole.MANAGER);

        String jsonRequest = objectMapper.writeValueAsString(roleRequestDto);

        // When
        MvcResult result = mockMvc.perform(patch("/users/{id}/role", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        UserDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserDto.class);
        assertNotNull(response);
        assertEquals(User.UserRole.MANAGER, response.getRole());
    }

    @WithUserDetails("user@gmail.com")
    @Test
    @DisplayName("Get my profile info")
    void getMyProfile_AuthenticatedUser_Success() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        UserDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserDto.class);
        assertNotNull(response);
        assertEquals("user@gmail.com", response.getEmail());
    }

    @WithUserDetails("user@gmail.com")
    @Test
    @DisplayName("Update my profile info")
    void updateMyProfile_ValidRequest_Success() throws Exception {
        // Given
        UpdateUserProfileRequestDto userProfileRequestDto = new UpdateUserProfileRequestDto();
        userProfileRequestDto.setEmail("newemail@gmail.com");
        userProfileRequestDto.setFirstName("UpdatedName");
        userProfileRequestDto.setLastName("UpdatedLastName");

        String jsonRequest = objectMapper.writeValueAsString(userProfileRequestDto);

        // When
        MvcResult result = mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        UserDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserDto.class);
        assertNotNull(response);
        assertEquals("newemail@gmail.com", response.getEmail());
        assertEquals("UpdatedName", response.getFirstName());
        assertEquals("UpdatedLastName", response.getLastName());
    }
}
