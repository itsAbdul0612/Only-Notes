package com.technerd.onlyNotes.controller;

import com.technerd.onlyNotes.DTOs.notesDTOSetup.NotesDTO;
import com.technerd.onlyNotes.DTOs.notesDTOSetup.NotesMapper;
import com.technerd.onlyNotes.DTOs.notesDTOSetup.NotesRequestDTO;
import com.technerd.onlyNotes.entity.Notes;
import com.technerd.onlyNotes.entity.User;
import com.technerd.onlyNotes.service.NotesService;
import com.technerd.onlyNotes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/notes")
@Tag(name = "Notes API", description = "Create, Read, Update and Delete Notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotesMapper mapper;

    // CREATE
    @PostMapping("/create-note")
    @Operation(summary = "Create notes using this endpoint.")
    public ResponseEntity<NotesDTO> createNote(@Valid @RequestBody NotesRequestDTO notesRequestDTO){
       try {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String name = authentication.getName();

           Notes entity = mapper.toEntity(notesRequestDTO);
           NotesDTO notesDTO = mapper.toDTO(entity);
           notesService.createNote(entity, name);

           return new ResponseEntity<>(notesDTO, HttpStatus.CREATED);
       } catch (Exception e) {
           log.error("Error occurred while saving notes: ", e);
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }


    // READ
    @Operation(summary = "Read notes by their Id using this endpoint.")
    @GetMapping("/read-note/{noteId}")
    public ResponseEntity<List<Notes>> readNote(@PathVariable ObjectId noteId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
            User userByUsername = userService.getUserByUsername(name);

            List<Notes> notes = userByUsername.getNotesList().stream()
                    .filter(x -> x.getId().equals(noteId))
                    .toList();
            if (!notes.isEmpty()) {
                return new ResponseEntity<>(notes, HttpStatus.OK);
            }
        } catch (Exception e) {
           log.error("Error while reading notes: ", e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // READ ALL
    @Operation(summary = "Retrieves all notes from database")
    @GetMapping("/get-all-notes")
    public ResponseEntity<List<Notes>> getAllNotes(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
            User userByUsername = userService.getUserByUsername(name);

            List<Notes> notesList = userByUsername.getNotesList();
            return ResponseEntity.ok(notesList);
        } catch (Exception e) {
            log.error("Error while getting all notes", e);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // UPDATE
    @PutMapping("/update-note/{noteId}")
    @Operation(summary = "Update notes using this endpoint.")
    public ResponseEntity<NotesDTO> updateNote(@PathVariable ObjectId noteId,
               @Valid @RequestBody NotesRequestDTO notesRequestDTO){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
            User userByUsername = userService.getUserByUsername(name);

            List<Notes> collect = userByUsername.getNotesList().stream()
                    .filter(x -> x.getId().equals(noteId))
                    .toList();

            if (!collect.isEmpty()){
                Optional<Notes> note = notesService.readNotes(noteId);
                if (note.isPresent()){
                    Notes existingNote = note.get();

                    if (!notesRequestDTO.getTitle().isEmpty() && !notesRequestDTO.getTitle().isBlank()){
                        existingNote.setTitle(notesRequestDTO.getTitle());
                    }
                    if (!notesRequestDTO.getContent().isEmpty() && !notesRequestDTO.getContent().isBlank()){
                        existingNote.setContent(notesRequestDTO.getContent());
                    }

                    existingNote.setFavourite(notesRequestDTO.isFavourite());
                    existingNote.setDate(LocalDateTime.now());

                    notesService.saveUpdatedNotes(existingNote, name);

                    Notes entity = mapper.toEntity(notesRequestDTO);
                    NotesDTO notesDTO = mapper.toDTO(entity);
                    return new ResponseEntity<>(notesDTO, HttpStatus.OK);
                }
            }
        } catch (Exception e){
            log.error("Error while updating note: ", e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // DELETE
    @Operation(summary = "Delete notes using this endpoint.")
    @DeleteMapping("/delete-note/{id}")
    public ResponseEntity<String> deleteNotes(@PathVariable ObjectId id){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();

            notesService.deleteNotes(id, name);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error while deleting note: ", e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // FAV NOTES

    @PatchMapping("/toggle-fav/{id}")
    @Operation(summary = "Add or remove a note to or from favourites.")
    public ResponseEntity<Notes> toggleFavourite(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        try {
            Optional<Notes> foundNote = notesService.readNotes(id);
            if (foundNote.isPresent() ){
                Notes note = foundNote.get();
               note.setFavourite(!note.isFavourite());
                notesService.saveUpdatedNotes(note, name);
                return new ResponseEntity<>(note, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error While toggling fav: ", e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/favourite-note")
    @Operation(summary = "read all favourite notes using this endpoint.")
    public ResponseEntity<List<Notes>> getAllFavNotes(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
            List<Notes> favouriteNotes = notesService.findFavouriteNotes(name);
            return new ResponseEntity<>(favouriteNotes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while finding fav: ", e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}