package com.access.zenchallenge.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.access.zenchallenge.constant.UserType;
import com.access.zenchallenge.dto.UserDto;
import com.access.zenchallenge.entity.UserEntity;
import com.access.zenchallenge.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserServiceTest {
    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Method under test: {@link UserService#getAllUser()}
     */
    @Test
    void testGetAllUser() {
        // Arrange
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<UserDto> actualAllUser = userService.getAllUser();

        // Assert
        verify(userRepository).findAll();
        assertTrue(actualAllUser.isEmpty());
    }

    /**
     * Method under test: {@link UserService#getAllUser()}
     */
    @Test
    void testGetAllUser2() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);

        ArrayList<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);

        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<UserDto>>any())).thenReturn(userDto);

        // Act
        List<UserDto> actualAllUser = userService.getAllUser();

        // Assert
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).findAll();
        assertEquals(1, actualAllUser.size());
        assertSame(userDto, actualAllUser.get(0));
    }

    /**
     * Method under test: {@link UserService#getAllUser()}
     */
    @Test
    void testGetAllUser3() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity2.setId(2L);
        userEntity2.setName("42");
        userEntity2.setUserType(UserType.AFFILIATE);

        ArrayList<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity2);
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);

        UserDto userDto = new UserDto();
        LocalDate createdDate = LocalDate.of(1970, 1, 1);
        userDto.setCreatedDate(createdDate);
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<UserDto>>any())).thenReturn(userDto);

        // Act
        List<UserDto> actualAllUser = userService.getAllUser();

        // Assert
        verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), isA(Class.class));
        verify(userRepository).findAll();
        assertEquals(2, actualAllUser.size());
        UserDto getResult = actualAllUser.get(1);
        LocalDate createdDate2 = getResult.getCreatedDate();
        assertEquals("1970-01-01", createdDate2.toString());
        assertEquals("Name", getResult.getName());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(UserType.EMPLOYEE, getResult.getUserType());
        assertSame(userDto, actualAllUser.get(0));
        assertSame(createdDate, createdDate2);
    }

    /**
     * Method under test: {@link UserService#getAllUser()}
     */
    @Test
    void testGetAllUser4() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);

        ArrayList<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<UserDto>>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userService.getAllUser());
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserService#getUserById(Long)}
     */
    @Test
    void testGetUserById() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<UserDto>>any())).thenReturn(userDto);

        // Act
        Optional<UserDto> actualUserById = userService.getUserById(1L);

        // Assert
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).findById(eq(1L));
        assertTrue(actualUserById.isPresent());
        assertSame(userDto, actualUserById.get());
    }

    /**
     * Method under test: {@link UserService#getUserById(Long)}
     */
    @Test
    void testGetUserById2() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<UserDto>>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(1L));
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).findById(eq(1L));
    }


    /**
     * Method under test: {@link UserService#createUserService(UserDto)}
     */
    @Test
    void testCreateUserService() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setUserType(UserType.EMPLOYEE);
        when(userRepository.save(Mockito.<UserEntity>any())).thenReturn(userEntity);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1));
        userEntity2.setId(1L);
        userEntity2.setName("Name");
        userEntity2.setUserType(UserType.EMPLOYEE);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<UserEntity>>any())).thenReturn(userEntity2);

        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);

        // Act
        UserEntity actualCreateUserServiceResult = userService.createUserService(userDto);

        // Assert
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).save(isA(UserEntity.class));
        assertSame(userEntity, actualCreateUserServiceResult);
    }

    /**
     * Method under test: {@link UserService#createUserService(UserDto)}
     */
    @Test
    void testCreateUserService2() {
        // Arrange
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<UserEntity>>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        UserDto userDto = new UserDto();
        userDto.setCreatedDate(LocalDate.of(1970, 1, 1));
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setUserType(UserType.EMPLOYEE);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUserService(userDto));
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Long)}
     */
    @Test
    void testDeleteUserById() {
        // Arrange
        doNothing().when(userRepository).deleteById(Mockito.<Long>any());
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);

        // Act
        userService.deleteUserById(1L);

        // Assert that nothing has changed
        verify(userRepository).deleteById(eq(1L));
        verify(userRepository).existsById(eq(1L));
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Long)}
     */
    @Test
    void testDeleteUserById2() {
        // Arrange
        doThrow(new IllegalArgumentException("foo")).when(userRepository).deleteById(Mockito.<Long>any());
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(1L));
        verify(userRepository).deleteById(eq(1L));
        verify(userRepository).existsById(eq(1L));
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Long)}
     */
    @Test
    void testDeleteUserById3() {
        // Arrange
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(1L));
        verify(userRepository).existsById(eq(1L));
    }
}
