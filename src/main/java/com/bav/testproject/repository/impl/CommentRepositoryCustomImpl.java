package com.bav.testproject.repository.impl;

import com.bav.testproject.entity.Comment;
import com.bav.testproject.repository.CommentRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public long getMaxId() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "_id"));
        query.limit(1);
        Comment maxObject = mongoTemplate.findOne(query, Comment.class);
        if (maxObject == null) {
            return 0L;
        }
        return maxObject.getId();
    }
}
