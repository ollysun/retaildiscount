package com.access.zenchallenge.controllers;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.access.zenchallenge.constant.UserType;
import com.access.zenchallenge.controller.UserController;
import com.access.zenchallenge.dto.UserDto;
import com.access.zenchallenge.entity.UserEntity;
import com.access.zenchallenge.repository.UserRepository;
import com.access.zenchallenge.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserControllerTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserController#getUserById(Long)}
     */
    @Test
    void testGetUserById() throws Exception {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);
        Optional<UserDto> ofResult = Optional.of(userDto);
        when(userService.getUserById(Mockito.<Long>any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"name\":\"Name\",\"userType\":\"EMPLOYEE\",\"createdDate\":[1970,1,1]}"));
    }

    /**
     * Method under test: {@link UserController#getUserById(Long)}
     */
    @Test
    void testGetUserById2() throws Exception {
        // Arrange
        Optional<UserDto> emptyResult = Optional.empty();
        when(userService.getUserById(Mockito.<Long>any())).thenReturn(emptyResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link UserController#createUser(UserDto)}
     */
    @Test
    void testCreateUser() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.<UserEntity>any())).thenReturn(userEntity);
        UserController userController = new UserController(new UserService(userRepository, new ModelMapper()));

        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);

        // Act
        UserEntity actualCreateUserResult = userController.createUser(userDto);

        // Assert
        verify(userRepository).save(isA(UserEntity.class));
        assertSame(userEntity, actualCreateUserResult);
    }

    /**
     * Method under test: {@link UserController#createUser(UserDto)}
     */
    @Test
    void testCreateUser2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.<UserEntity>any())).thenReturn(userEntity);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity2.setId(1L);
        userEntity2.setName("Name");
        userEntity2.setUserType(UserType.EMPLOYEE);
        ModelMapper modelMapper = mock(ModelMapper.class);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<UserEntity>>any())).thenReturn(userEntity2);
        UserController userController = new UserController(new UserService(userRepository, modelMapper));

        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);

        // Act
        UserEntity actualCreateUserResult = userController.createUser(userDto);

        // Assert
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).save(isA(UserEntity.class));
        assertSame(userEntity, actualCreateUserResult);
    }

    /**
     * Method under test: {@link UserController#createUser(UserDto)}
     */
    @Test
    void testCreateUser3() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);
        UserService userService = mock(UserService.class);
        when(userService.createUserService(Mockito.<UserDto>any())).thenReturn(userEntity);
        UserController userController = new UserController(userService);

        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);

        // Act
        UserEntity actualCreateUserResult = userController.createUser(userDto);

        // Assert
        verify(userService).createUserService(isA(UserDto.class));
        assertSame(userEntity, actualCreateUserResult);
    }

    /**
     * Method under test: {@link UserController#createUser(UserDto)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateUser4() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDate` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.access.zenchallenge.dto.UserDto["createdDate"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1330)
        //       at com.fasterxml.jackson.databind.ser.impl.UnsupportedTypeSerializer.serialize(UnsupportedTypeSerializer.java:35)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:732)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:770)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:183)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:502)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:341)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4799)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:4040)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);
        String content = (new ObjectMapper()).writeValueAsString(userDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
    }

    /**
     * Method under test: {@link UserController#deleteUser(Long)}
     */
    @Test
    void testDeleteUser() throws Exception {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);
        Optional<UserDto> ofResult = Optional.of(userDto);
        doNothing().when(userService).deleteUserById(Mockito.<Long>any());
        when(userService.getUserById(Mockito.<Long>any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserController#deleteUser(Long)}
     */
    @Test
    void testDeleteUser2() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUserById(Mockito.<Long>any());
        Optional<UserDto> emptyResult = Optional.empty();
        when(userService.getUserById(Mockito.<Long>any())).thenReturn(emptyResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link UserController#getAllUsers()}
     */
    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        when(userService.getAllUser()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserController#updateUser(Long, UserDto)}
     */
    @Test
    void testUpdateUser() throws Exception {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);
        Optional<UserDto> ofResult = Optional.of(userDto);

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);
        when(userService.createUserService(Mockito.<UserDto>any())).thenReturn(userEntity);
        when(userService.getUserById(Mockito.<Long>any())).thenReturn(ofResult);

        UserDto userDto2 = new UserDto();
        userDto2.setCreatedDate(null);
        userDto2.setId(1L);
        userDto2.setName("Name");
        userDto2.setUserType(UserType.EMPLOYEE);
        String content = (new ObjectMapper()).writeValueAsString(userDto2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"name\":\"Name\",\"userType\":\"EMPLOYEE\",\"createdDate\":[1970,1,1]}"));
    }

    /**
     * Method under test: {@link UserController#updateUser(Long, UserDto)}
     */
    @Test
    void testUpdateUser2() throws Exception {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);
        when(userService.createUserService(Mockito.<UserDto>any())).thenReturn(userEntity);
        Optional<UserDto> emptyResult = Optional.empty();
        when(userService.getUserById(Mockito.<Long>any())).thenReturn(emptyResult);

        UserDto userDto = new UserDto();
        userDto.setCreatedDate(null);
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);
        String content = (new ObjectMapper()).writeValueAsString(userDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
