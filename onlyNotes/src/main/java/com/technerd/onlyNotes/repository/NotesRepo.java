package com.technerd.onlyNotes.repository;

import com.technerd.onlyNotes.entity.Notes;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotesRepo extends
        MongoRepository<Notes, ObjectId>,
        PagingAndSortingRepository<Notes, ObjectId> {

   Page<Notes> findAllByUserId(ObjectId userId, Pageable pageable);
}
