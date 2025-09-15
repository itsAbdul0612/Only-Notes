package com.technerd.onlyNotes.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "user")
public class User {

    @Id
    private ObjectId id;

    private String email;

    @Schema(description ="User's username")
    @Indexed(unique = true)
    private String userName;


    @Schema(description = "User's password")
    private String password;


    private LocalDateTime date;

    private List<String> role = new ArrayList<>();

    @DBRef
    private List<Notes> notesList = new ArrayList<>();
}
