package com.technerd.onlyNotes.DTOs.notesDTOSetup;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

import static javax.print.attribute.Size2DSyntax.MM;

@Data
public class NotesRequestDTO {

    private String title;
//    private ObjectId userId;
    private String content;
    private boolean favourite;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;

}
