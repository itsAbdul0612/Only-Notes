package com.technerd.onlyNotes.controller;

import com.technerd.onlyNotes.DTOs.usersDTOSetup.UserDTO;
import com.technerd.onlyNotes.DTOs.usersDTOSetup.UserMapper;
import com.technerd.onlyNotes.DTOs.usersDTOSetup.UserRequestDTO;
import com.technerd.onlyNotes.entity.Notes;
import com.technerd.onlyNotes.entity.User;
import com.technerd.onlyNotes.service.NotesService;
import com.technerd.onlyNotes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "Read, Update and Delete Users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotesService notesService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


//    // READ
//    @GetMapping
//    public void getUser(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        userService.getUserByUsername(username);
//    }


    // UPDATE
    @PutMapping("/update-user")
    @Operation(summary = "Update username, password or both.")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
            User userByUsername = userService.getUserByUsername(name);

            String incomingUsername = userRequestDTO.getUserName();
            String incomingPassword = userRequestDTO.getPassword();

            if (!incomingUsername.isBlank()){
                userByUsername.setUserName(incomingUsername);
            }
            if (!incomingPassword.isBlank()){
                userByUsername.setPassword(passwordEncoder.encode(incomingPassword));
            }

            UserMapper mapper = new UserMapper();

            User user = mapper.toEntity(userRequestDTO);
            UserDTO userDTO = mapper.toDTO(user);

            userService.saveUser(userByUsername);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while updating user: ", e);
        }
        return ResponseEntity.badRequest().body("Validation Failed!");
    }


    // DELETE
    @DeleteMapping("/delete-user")
    @Operation(summary = "Delete your account.")
    public ResponseEntity<String> deleteUser(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
            if (name != null) {
                List<Notes> notesList = userService.getUserByUsername(name).getNotesList().stream().toList();

                notesService.deleteAll(notesList);
                userService.deleteUser(name);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception _){}
        return ResponseEntity.badRequest().body("Validation Failed!");
    }

}
