package com.bav.testproject.users;

import org.springframework.data.mongodb.repository.MongoRepository;

//Репозиторий пользователя
public interface UserRepository extends MongoRepository<User, Long> {

    //Поиск по имени
    User findByUsername(String username);
}
