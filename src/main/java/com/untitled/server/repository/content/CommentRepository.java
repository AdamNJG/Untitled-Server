package com.untitled.server.repository.content;

import com.untitled.server.domain.content.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
