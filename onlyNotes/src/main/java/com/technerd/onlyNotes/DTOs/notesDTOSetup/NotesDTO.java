package com.technerd.onlyNotes.DTOs.notesDTOSetup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.technerd.onlyNotes.entity.Notes;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
public class NotesDTO {

    private String id;
//    private ObjectId userId;
    private String title;
    private String content;
    private boolean favourite;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;


}
