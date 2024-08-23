package com.kropsz.mslastfm.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kropsz.mslastfm.data.model.UserData;

public interface UserDataRepository extends MongoRepository<UserData, String> {

}
