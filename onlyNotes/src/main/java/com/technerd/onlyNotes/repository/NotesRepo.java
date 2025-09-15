package com.technerd.onlyNotes.repository;

import com.technerd.onlyNotes.entity.Notes;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepo extends MongoRepository<Notes, ObjectId> {
//    void deleteAll(List<Notes> notes);
}
