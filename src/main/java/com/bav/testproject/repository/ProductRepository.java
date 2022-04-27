package com.bav.testproject.repository;

import com.bav.testproject.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

//Запросы к БД для товаров магазина
public interface ProductRepository extends MongoRepository<Product, Long> {

    //поиск по id
    Product findById(long id);

    //Удалить товар по id
    void deleteById(long id);
}
