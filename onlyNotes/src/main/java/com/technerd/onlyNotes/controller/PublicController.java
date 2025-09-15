package com.technerd.onlyNotes.controller;

import com.technerd.onlyNotes.DTOs.usersDTOSetup.UserMapper;
import com.technerd.onlyNotes.DTOs.usersDTOSetup.UserRequestDTO;
import com.technerd.onlyNotes.entity.User;
import com.technerd.onlyNotes.DTOs.usersDTOSetup.UserDTO;
import com.technerd.onlyNotes.service.UserDetailServiceImpl;
import com.technerd.onlyNotes.service.UserService;
import com.technerd.onlyNotes.utility.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    // CREATE
    @PostMapping("/signup")
    @Operation(summary = "Creates an account for the app.")
    public ResponseEntity<UserDTO> signUp(@RequestBody UserRequestDTO userRequestDTO){
        try {
            UserMapper userMapper = new UserMapper();
            User user = userMapper.toEntity(userRequestDTO);
            userService.createUser(user);
            UserDTO dto = userMapper.toDTO(user);
            return ResponseEntity.ok(dto);
        } catch (Exception e){
            log.error("Error occurred while saving new user: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDTO userRequestDTO){
        try {

            // Authenticating...
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequestDTO.getUserName(), userRequestDTO.getPassword()));

            // Loading user from DB...
            UserDetails userDetails = userDetailService.loadUserByUsername(userRequestDTO.getUserName());

            // Generating JWT Token using userName...
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(jwt);

        } catch (AuthenticationException e) {
            log.error("Error while generating token: ", e);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
