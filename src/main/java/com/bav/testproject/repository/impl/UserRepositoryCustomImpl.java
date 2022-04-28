package com.bav.testproject.repository.impl;

import com.bav.testproject.entity.User;
import com.bav.testproject.repository.UserRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public long getMaxId() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "_id"));
        query.limit(1);
        User maxObject = mongoTemplate.findOne(query, User.class);
        if (maxObject == null) {
            return 0L;
        }
        return maxObject.getId();
    }
}
