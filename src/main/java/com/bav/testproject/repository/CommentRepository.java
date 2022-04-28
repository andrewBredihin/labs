package com.bav.testproject.repository;

import com.bav.testproject.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, Long> {

    List<Comment> findAllByProductId(long productId);
}
