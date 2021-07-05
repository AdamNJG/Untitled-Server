package com.untitled.server.service;

import com.untitled.server.domain.auth.User;
import com.untitled.server.domain.content.Comment;
import com.untitled.server.domain.content.Post;
import com.untitled.server.repository.auth.UserRepository;
import com.untitled.server.repository.content.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class CommentService {

    @Autowired
    UserRepository userRepository;

    public Comment commentFactory(String content,String username){
        User user = userRepository.findByUsername(username);
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        return comment;
    }

}
