package com.kropsz.mslastfm.data.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kropsz.mslastfm.data.model.UserData;

public interface UserDataRepository extends MongoRepository<UserData, String> {

    Optional<UserData> findByUser(String username);

}
