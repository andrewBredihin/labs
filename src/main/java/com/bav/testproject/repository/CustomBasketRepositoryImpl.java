package com.bav.testproject.repository;

import com.bav.testproject.entity.ProductOnBasket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomBasketRepositoryImpl implements CustomBasketRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean checkProductOnBasket(long userId, long productId) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "userId")).addCriteria(Criteria.where("userId").is(userId).and("productId").is(productId));
        ProductOnBasket product = mongoTemplate.findOne(query, ProductOnBasket.class);

        if (product != null) return true;
        else return false;
    }

    @Override
    public long getMaxId() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "_id"));
        query.limit(1);
        ProductOnBasket maxObject = mongoTemplate.findOne(query, ProductOnBasket.class);
        if (maxObject == null) {
            return 0L;
        }
        return maxObject.getId();
    }
}
