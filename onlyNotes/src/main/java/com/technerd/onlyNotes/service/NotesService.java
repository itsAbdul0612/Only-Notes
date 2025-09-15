package com.technerd.onlyNotes.service;

import com.technerd.onlyNotes.entity.Notes;
import com.technerd.onlyNotes.entity.User;
import com.technerd.onlyNotes.repository.NotesRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotesService {

    @Autowired
    private NotesRepo notesRepo;

    @Autowired
    private UserService userService;

    // CREATE
    public void createNote(Notes note, String username){

        User user = userService.getUserByUsername(username);
        note.setTitle(note.getTitle());
        note.setContent(note.getContent());
        note.setFavourite(note.isFavourite());
        note.setDate(LocalDateTime.now());

        user.getNotesList().add(note);
        notesRepo.save(note);
        userService.saveUser(user);
    }


    // READ
    public Optional<Notes> readNotes(ObjectId id){
       return notesRepo.findById(id);
    }

    // UPDATE
    public void saveUpdatedNotes(Notes notes, String username){
        Notes save = notesRepo.save(notes);
        User user = userService.getUserByUsername(username);
        user.getNotesList().add(save);
    }

    // DELETE
    public void deleteNotes(ObjectId id, String username){
        User userByUsername = userService.getUserByUsername(username);
        boolean removeRefFromUser = userByUsername.getNotesList().removeIf(x -> x.getId().equals(id));
        if (removeRefFromUser){
            userService.saveUser(userByUsername);
            notesRepo.deleteById(id);
        }
    }

    // DELETE ALL
    public void deleteAll(List<Notes> notes){
        notesRepo.deleteAll(notes);
    }


    // Favourite Note
    public List<Notes> findFavouriteNotes(String username){
        User userByUsername = userService.getUserByUsername(username);
        return userByUsername.getNotesList().stream()
                .filter(Notes::isFavourite)
                .toList();
    }

}
