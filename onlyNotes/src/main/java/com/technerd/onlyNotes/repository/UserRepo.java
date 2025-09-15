package com.technerd.onlyNotes.repository;

import com.technerd.onlyNotes.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId> {
    void deleteByUserName(String userName);
    User findUserByUserName(String userName);
}
