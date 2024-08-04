package com.access.zenchallenge.controller;


import com.access.zenchallenge.dto.UserDto;
import com.access.zenchallenge.entity.UserEntity;
import com.access.zenchallenge.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> userDto = userService.getUserById(id);
        return userDto.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserEntity createUser(@RequestBody @Valid UserDto userDto) {

        return userService.createUserService(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id,
                                                 @RequestBody @Valid UserDto userDetails) {
        Optional<UserDto> userDto = userService.getUserById(id);

        if (userDto.isPresent()) {
            UserDto existingUserEntity = userDto.get();
            existingUserEntity.setName(userDetails.getName());
            existingUserEntity.setUserType(userDetails.getUserType());
            return ResponseEntity.ok(userService.createUserService(existingUserEntity));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
