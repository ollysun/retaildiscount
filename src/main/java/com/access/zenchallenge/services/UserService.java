package com.access.zenchallenge.services;

import com.access.zenchallenge.dto.UserDto;
import com.access.zenchallenge.entity.UserEntity;
import com.access.zenchallenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> getAllUser() {
        return userRepository.findAll().stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long id) {

        return Optional.ofNullable(userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + id)))
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    @Transactional
    public UserEntity createUserService(UserDto userDto) {

        if (userDto == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        return userRepository.save(userEntity);
    }

    @Transactional
    public void deleteUserById(Long id) {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User not found with id: " + id);
        }


    }
}
