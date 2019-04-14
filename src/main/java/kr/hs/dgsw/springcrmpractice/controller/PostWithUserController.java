package kr.hs.dgsw.springcrmpractice.controller;

import kr.hs.dgsw.springcrmpractice.domain.Post;
import kr.hs.dgsw.springcrmpractice.protocol.PostWithUser;
import kr.hs.dgsw.springcrmpractice.service.PostWithUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostWithUserController {

    private final PostWithUserService postWithUserService;

    @Autowired
    public PostWithUserController(PostWithUserService postWithUserService) {
        this.postWithUserService = postWithUserService;
    }

    @GetMapping("/post")
    public List<PostWithUser> listPosts() {
        return postWithUserService.listPosts();
    }

    @GetMapping("/post/{id}")
    public PostWithUser getPost(@PathVariable Long id) {
        return postWithUserService.getPost(id);
    }

    @PostMapping("/post")
    public PostWithUser addPost(@RequestBody Post post) {
        return postWithUserService.addPost(post);
    }

    @PutMapping("/post/{id}")
    public PostWithUser updatePost(@PathVariable Long id, @RequestBody Post post) {
        return postWithUserService.updatePost(id, post);
    }

    @DeleteMapping("/post/{id}")
    public boolean deletePost(@PathVariable Long id) {
        return postWithUserService.deletePost(id);
    }
}
