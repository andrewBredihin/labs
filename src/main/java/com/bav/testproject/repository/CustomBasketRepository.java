package com.bav.testproject.repository;

//Настраиваемый репозиторий для продуктов в корзинах пользователей
public interface CustomBasketRepository {

    //Проверка на наличие в корзине
    boolean checkProductOnBasket(long userId, long productId);

    //Получить максимальный Id
    long getMaxId();
}
