package com.technerd.onlyNotes.DTOs.usersDTOSetup;

import com.technerd.onlyNotes.entity.User;
import lombok.Data;

@Data
public class UserMapper {

    // USER

    public UserDTO toDTO(User user){

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }

    public User toEntity(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setPassword(userRequestDTO.getPassword());
        user.setUserName(userRequestDTO.getUserName());
        user.setEmail(userRequestDTO.getEmail());

        return user;
    }
}
