package com.technerd.onlyNotes.DTOs.notesDTOSetup;

import com.technerd.onlyNotes.entity.Notes;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
public class NotesDTO {

    private String id;
    private String title;
    private String content;
    private boolean favourite;
    private LocalDateTime date;


}
