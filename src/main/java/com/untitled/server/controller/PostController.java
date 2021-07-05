package com.untitled.server.controller;

import com.untitled.server.configuration.JwtUtils;
import com.untitled.server.domain.content.Post;
import com.untitled.server.model.content.PostDTO;
import com.untitled.server.repository.content.PostRepository;
import com.untitled.server.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/content")
public class PostController {


    @Autowired
    private PostRepository postRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PostService postService;


    @GetMapping("/getpost")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public Iterable<Post> getPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/getpostdesc")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public Iterable<Post> getPostsDesc() {
        return postRepository.findTop100ByOrderByDateCreatedDesc();
    }


    @GetMapping("/getpostasc")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public Iterable<Post> getPostsAsc() {
        return postRepository.findTop100ByOrderByDateCreatedAsc();
    }


    @GetMapping("/getpostpop")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public Iterable<Post> getPopularPosts() {
        return postRepository.findTop100ByOrderByPopularityDesc();
    }


    @RequestMapping(value = "/post", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> savePost(@RequestBody String content, HttpServletRequest req) {
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
        Post post = postService.postFactory(content, username);
        try {
            if (post instanceof Post) {
                postRepository.save(post);
                return ResponseEntity.ok("Post posted");
            } else {
                return ResponseEntity.ok("post failed");
            }
        } catch (Exception e) {
            return ResponseEntity.ok(e);
        }

    }

    @PostMapping("/giveprops")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> setProps(@RequestBody long postId, HttpServletRequest req) {
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
        try {
            Post post = postRepository.findById(postId).orElse(new Post());
            if (post.getContent() != null) {
                ArrayList props = post.getStarList();
                if (props != null) {
                    if (props.contains(username)) {
                        props.remove(username);
                        int pop = post.getPopularity();
                        post.setPopularity(--pop);
                        post.setStarList(props);
                        postRepository.save(post);
                        return ResponseEntity.ok("Star removed");
                    }
                } else {
                    props = new ArrayList();
                }
                props.add(username);
                System.out.println(props);
                post.setStarList(props);
                int pop = post.getPopularity();
                post.setPopularity(++pop);
                postRepository.save(post);
                return ResponseEntity.ok("Star added");
            }
            return ResponseEntity.ok("Post not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("There has been a server error, please log this with support");
        }
    }

    @DeleteMapping("/deletepost")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public void deletePost(@RequestBody long id) {

        Post post = postRepository.findById(id).orElse(new Post());
        postRepository.delete(post);
    }

    @PatchMapping("/updatepost")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public void updatePost(@RequestBody PostDTO postDTO) {
        Post post = postRepository.findById(postDTO.getId()).orElse(new Post());
        post.setContent(postDTO.getContent());
        postRepository.save(post);

    }
}
