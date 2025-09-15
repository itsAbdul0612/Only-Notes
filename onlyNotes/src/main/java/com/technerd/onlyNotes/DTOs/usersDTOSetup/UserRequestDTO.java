package com.technerd.onlyNotes.DTOs.usersDTOSetup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserRequestDTO{

    @NonNull
    @NotEmpty
    private String userName;

    @NotEmpty
    @NonNull
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Email(message = "Invalid email!")
    private String email;
}