package com.technerd.onlyNotes.DTOs.notesDTOSetup;

import com.technerd.onlyNotes.entity.Notes;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class NotesMapper {

    public NotesDTO toDTO(Notes notes){
        NotesDTO dto = new NotesDTO();

        dto.setTitle(notes.getTitle());
        dto.setContent(notes.getContent());
        dto.setDate(notes.getDate());
        dto.setFavourite(notes.isFavourite());

        return dto;
    }

    public Notes toEntity(NotesRequestDTO notesRequestDTO){
        Notes notes = new Notes();

        notes.setTitle(notesRequestDTO.getTitle());
        notes.setContent(notesRequestDTO.getContent());
        notes.setFavourite(notes.isFavourite());
        notes.setDate(notesRequestDTO.getDate());

        return notes;
    }

}
