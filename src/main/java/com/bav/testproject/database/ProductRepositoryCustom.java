package com.bav.testproject.database;

import java.util.List;

//Настраиваемые запросы к БД
public interface ProductRepositoryCustom {

    //Получить максимальный id
    long getMaxId();

    //Получить все объекты по тегам
    List<Product> findByTags(String tags);

    //Получить все товары из корзины определенного пользователя
    List<Product> findFromBasket(long userId);
}
