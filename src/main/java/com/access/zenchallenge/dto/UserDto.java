package com.access.zenchallenge.dto;

import com.access.zenchallenge.constant.UserType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;


@Data
public class UserDto {
    private Long id;
    @NotEmpty(message = "Please enter the name of the user")
    private String name;
    @NotNull(message = "Please specify user type")
    private UserType userType;
    private LocalDate createdDate;
}
