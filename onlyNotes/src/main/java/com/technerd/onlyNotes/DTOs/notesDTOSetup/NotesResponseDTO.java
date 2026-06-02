package com.technerd.onlyNotes.DTOs.notesDTOSetup;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotesResponseDTO {
    
    private String title;
    private String content;
    private boolean favourite;
    private LocalDateTime createdAt;

}
