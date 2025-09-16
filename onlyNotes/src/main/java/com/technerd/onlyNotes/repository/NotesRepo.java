package com.technerd.onlyNotes.repository;

import com.technerd.onlyNotes.entity.Notes;
import com.technerd.onlyNotes.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepo extends MongoRepository<Notes, ObjectId> {
//    Page<Notes> findAllByUserName(User user, Pageable pageable);
}
