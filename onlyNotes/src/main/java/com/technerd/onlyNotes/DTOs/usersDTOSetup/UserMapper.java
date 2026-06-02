package com.technerd.onlyNotes.DTOs.usersDTOSetup;

import com.technerd.onlyNotes.entity.User;
import lombok.Data;

@Data
public class UserMapper {

    // USER

    public UserResponseDTO toDTO(User user){

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserName(user.getUserName());
        userResponseDTO.setEmail(user.getEmail());

        return userResponseDTO;
    }

    public User toEntity(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setPassword(userRequestDTO.getPassword());
        user.setUserName(userRequestDTO.getUserName());
        user.setEmail(userRequestDTO.getEmail());

        return user;
    }
}
