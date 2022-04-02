package com.bav.testproject.database;

import java.util.List;

//Настраиваемые запросы к БД
public interface ProductRepositoryCustom {

    long getMaxId();

    List<Product> findByTags(String tags);
}
