package com.bav.testproject.database;

import org.springframework.data.mongodb.repository.MongoRepository;

//Запросы к БД для товаров магазина
public interface ProductRepository extends MongoRepository<Product, Long> {

    //поиск по id
    Product findById(long id);

    //Удалить товар по id
    void deleteById(long id);
}
