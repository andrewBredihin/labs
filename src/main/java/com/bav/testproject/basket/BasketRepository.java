package com.bav.testproject.basket;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

//Репозиторий для продуктов в корзинах пользователей
public interface BasketRepository extends MongoRepository<ProductOnBasket, Long> {

    //поиск по id
    ProductOnBasket findById(long id);

    //Выборка по пользователю
    List<ProductOnBasket> findAllByUserId(long userId);

    //Поиск конкретного объекта (для удаления)
    ProductOnBasket findByUserIdAndProductId(long userId, long productId);
}
