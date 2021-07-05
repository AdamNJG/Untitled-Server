package com.untitled.server.repository.content;

import com.untitled.server.domain.auth.User;
import com.untitled.server.domain.content.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    Post findByUser(User user);


    List<Post> findTop100ByOrderByDateCreatedDesc();

    List<Post> findTop100ByOrderByDateCreatedAsc();

    List<Post> findTop100ByOrderByPopularityDesc();



}
