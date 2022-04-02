package com.bav.testproject.database;

import org.springframework.data.mongodb.repository.MongoRepository;

//Запросы к БД
public interface ProductRepository extends MongoRepository<Product, Long> {

    Product findById(long id);
    Product findByTitle(String title);

    void deleteById(long id);
}
