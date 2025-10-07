package com.technerd.onlyNotes.controller;

import com.technerd.onlyNotes.DTOs.usersDTOSetup.UserMapper;
import com.technerd.onlyNotes.DTOs.usersDTOSetup.UserRequestDTO;
import com.technerd.onlyNotes.entity.AuthResponse;
import com.technerd.onlyNotes.entity.User;
import com.technerd.onlyNotes.DTOs.usersDTOSetup.UserDTO;
import com.technerd.onlyNotes.service.RedisService;
import com.technerd.onlyNotes.service.UserDetailServiceImpl;
import com.technerd.onlyNotes.service.UserService;
import com.technerd.onlyNotes.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Public APIs", description = "Public related APIs like Sign-up and Login.")
@RequestMapping("/public")
public class PublicController {


    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailServiceImpl userDetailService;

    private final JwtService jwtService;

    private final RedisService redisService;

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
    @Operation(summary = "Login to your account.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO userRequestDTO) {
        try {

            // Authenticating...
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequestDTO.getUserName(), userRequestDTO.getPassword()));

            // Loading user from DB...
            UserDetails userDetails = userDetailService.loadUserByUsername(userRequestDTO.getUserName());

            User userByUsername = userService.getUserByUsername(userDetails.getUsername());
            ObjectId id = userByUsername.getId();

            String redisKey = userDetails.getUsername() + id.toString();

            // Try to get refresh token from redis...
            String refreshTokenFromRedis = redisService.getRefreshTokenFromRedis(redisKey);
            if (refreshTokenFromRedis != null) {
                return ResponseEntity.ok(refreshTokenFromRedis);
            } else {

                // Generating JWT Token using userName...
                String accessToken = jwtService.generateToken(userDetails.getUsername());
                String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

                redisService.setRefreshToken(redisKey, refreshToken); // 7 Days.

                return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));

            }

            } catch(AuthenticationException e){
                log.error("Error while generating token: ", e);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @Operation(summary = "Generates a refresh token to login")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody AuthResponse response){

        try {
            String refreshToken = response.getRefreshToken();

            if (jwtService.validateToken(refreshToken)) {
                String username = jwtService.extractUsername(refreshToken);

                User userByUsername = userService.getUserByUsername(username);
                ObjectId id = userByUsername.getId();

                String redisKey = username + id.toString(); // Redis Key.

                // Deletes Existing Token (Rotation)
                redisService.revokeToken(redisKey);

                // Generate New Tokens
                String newAccessToken = jwtService.generateToken(username);
                String newRefreshToken = jwtService.generateRefreshToken(username);

                redisService.setRefreshToken(redisKey, newRefreshToken);

                return ResponseEntity.ok(Map.of(
                        "accessToken: ", newAccessToken,
                        "refreshToken: ", newRefreshToken
                ));
            }
        } catch (Exception e) {
            log.error("Error while validating refresh token.", e);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
    }

    @Operation(summary = "Deletes the refresh token from redis.")
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody AuthResponse responce){

        try {
            String refreshToken = responce.getRefreshToken();
            if (jwtService.validateToken(refreshToken)){
                String username = jwtService.extractUsername(refreshToken);

                // To get userId of the user.
                User userByUsername = userService.getUserByUsername(username);
                String userId = userByUsername.getId().toString();

                redisService.revokeToken(username + userId);

                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            log.error("Error while logging out token", e);
        }
        return ResponseEntity.badRequest().body("invalid");
    }
}