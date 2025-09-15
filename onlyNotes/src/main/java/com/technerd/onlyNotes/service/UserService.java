package com.technerd.onlyNotes.service;

import com.technerd.onlyNotes.entity.Notes;
import com.technerd.onlyNotes.entity.User;
import com.technerd.onlyNotes.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // CREATE Users
  public void createUser(User user){
    try {
        user.setUserName(user.getUserName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail());
        user.setRole(List.of("USER"));
        user.setDate(LocalDateTime.now());

        userRepo.save(user);
    }catch (Exception e){
        log.error("Error occurred while saving user: ",e);
    }
  }

    public void saveUser(User user){
        try {
            userRepo.save(user);
        }catch (Exception e){
            log.error("Error occurred while saving user: ",e);
        }
    }



  // READ Users
  // UPDATE Users
    public User getUserByUsername(String name){
      return userRepo.findUserByUserName(name);
    }


  // DELETE Users
    public void deleteUser(String username){
        userRepo.deleteByUserName(username);
    }

}
