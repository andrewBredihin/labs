package com.bav.testproject.repository;

import com.bav.testproject.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

//Репозиторий пользователя
public interface UserRepository extends MongoRepository<User, Long> {

    //Поиск по имени
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(long id);
}
