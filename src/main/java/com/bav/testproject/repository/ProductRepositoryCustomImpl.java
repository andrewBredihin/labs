package com.bav.testproject.repository;

import com.bav.testproject.entity.Product;
import com.bav.testproject.repository.BasketRepository;
import com.bav.testproject.entity.ProductOnBasket;
import com.bav.testproject.repository.ProductRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

//Настраиваемые запросы к БД
@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BasketRepository basketRepository;

    @Override
    public long getMaxId() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "id"));
        query.limit(1);
        Product maxObject = mongoTemplate.findOne(query, Product.class);
        if (maxObject == null) {
            return 0L;
        }
        return maxObject.getId();
    }

    @Override
    public List<Product> findByTags(String tags) {
        String criteria = "{ tags: { $all : [";

        String[] tagArray = tags.split(" ");
        for (String tag : tagArray){
            criteria += " /^" + tag + "$/i,";
        }

        criteria += "] } }";
        System.out.println(criteria + " ||||| " + tagArray.length);

        BasicQuery basicQuery = new BasicQuery(criteria);

        return mongoTemplate.find(basicQuery, Product.class);
    }

    @Override
    public List<Product> findFromBasket(long userId) {
        List<Product> products = new ArrayList<>();

        List<ProductOnBasket> list = basketRepository.findAllByUserId(userId);
        for (ProductOnBasket x : list)
            products.add(mongoTemplate.findById(x.getProductId(), Product.class));

        return products;
    }
}
