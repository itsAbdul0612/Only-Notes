package com.technerd.onlyNotes.service;

import com.technerd.onlyNotes.entity.Notes;
import com.technerd.onlyNotes.entity.User;
import com.technerd.onlyNotes.repository.NotesRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NotesService {

    @Autowired
    private NotesRepo notesRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;


    // CREATE
    public void createNote(Notes note, String username){

        User user = userService.getUserByUsername(username);

        note.setUserId(user.getId());
        note.setTitle(note.getTitle());
        note.setContent(note.getContent());
        note.setFavourite(note.isFavourite());
        note.setCreatedAt(LocalDateTime.now());

        user.getNotesList().add(note);
        notesRepo.save(note);
        userService.saveUser(user);
    }


    // READ
    public Optional<Notes> readNotes(ObjectId id){
       return notesRepo.findById(id);
    }

    //READ ALL
    public Page<Notes> readAllNotes(String username, int pageNumber, int pageSize){
        User user = userService.getUserByUsername(username);
        ObjectId userId = user.getId();
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by("createdAt").descending());
        return notesRepo.findAllByUserId(userId, pageable);
    }

    // UPDATE
    public void saveUpdatedNotes(Notes notes, String username){
        Notes saveInDb = notesRepo.save(notes);
        User user = userService.getUserByUsername(username);
        user.getNotesList().add(saveInDb);
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
        Optional<List<Notes>> favouriteNotesFromRedis = redisService.getFavouriteNotesFromRedis(username);
            if (favouriteNotesFromRedis.isPresent()) {
                return favouriteNotesFromRedis.get();
            } else {
                List<Notes> list = userByUsername.getNotesList().stream()
                    .filter(Notes::isFavourite)
                    .toList();
                redisService.set(username, list);
                return list;
            }
    }

}
