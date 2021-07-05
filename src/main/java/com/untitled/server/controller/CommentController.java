package com.untitled.server.controller;


import com.untitled.server.configuration.JwtUtils;
import com.untitled.server.domain.content.Comment;
import com.untitled.server.domain.content.Post;
import com.untitled.server.model.requests.CommentRequest;
import com.untitled.server.repository.content.CommentRepository;
import com.untitled.server.repository.content.PostRepository;
import com.untitled.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/content")
public class CommentController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    PostRepository postRepository;

    @RequestMapping(value = "/comment", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> savePost(@RequestBody CommentRequest comReq, HttpServletRequest req) {
        Cookie cookies[] = req.getCookies();
        String token = null;

        if (cookies != null && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (c.getName().contains("token")) {
                    token = c.getValue();
                }
            }
        }
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Comment comment = commentService.commentFactory(comReq.getContent(), username);
        Post post = postRepository.findById(comReq.getPostId()).orElse(null);
        try {
            if (post instanceof Post) {
                comment.setPost(post);
                commentRepository.save(comment);
                List<Comment> commentsList = post.getComments();
                commentsList.add(comment);
                post.setComments(commentsList);
                post.setPopularity(post.getPopularity() + 1);
                postRepository.save(post);
                return ResponseEntity.ok("Comment posted");
            } else {
                return ResponseEntity.ok("Post not found");
            }
        } catch (Exception e) {
            return ResponseEntity.ok(e);
        }

    }

    @DeleteMapping("/deletecomment")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public void deletePost(@RequestBody long id) {

        Comment comment = commentRepository.findById(id).orElse(new Comment());
        commentRepository.delete(comment);
    }

    @PatchMapping("/updatecomment")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public void updatePost(@RequestBody CommentRequest req) {
        Comment comment = commentRepository.findById(req.getId()).orElse(new Comment());
        comment.setContent(req.getContent());
        commentRepository.save(comment);

    }
}
