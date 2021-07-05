package com.untitled.server.service;

import com.untitled.server.domain.auth.User;
import com.untitled.server.domain.content.Post;
import com.untitled.server.repository.auth.UserRepository;
import com.untitled.server.repository.content.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class PostService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    public Post postFactory(String content, String username){
          Post post = new Post();
          User user = userRepository.findByUsername(username);
          post.setUser(user);
          post.setContent(content);
          return post;
    }


}
