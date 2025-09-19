package com.technerd.onlyNotes.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document(collection = "notes")
public class Notes implements Serializable {

    @Id
    private ObjectId id;
//    @JsonSerialize(using = ToStringSerializer.class)

    private String title;
    private String content;
    private boolean favourite;

    private LocalDateTime date;
}
