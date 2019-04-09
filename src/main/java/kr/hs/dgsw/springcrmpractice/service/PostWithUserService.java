package kr.hs.dgsw.springcrmpractice.service;

import kr.hs.dgsw.springcrmpractice.domain.Post;
import kr.hs.dgsw.springcrmpractice.protocol.PostWithUser;

import java.util.List;

public interface PostWithUserService {
    List<PostWithUser> listPosts();
    PostWithUser getPost(Long id);
    PostWithUser addPost(Post post);
    PostWithUser updatePost(Long id, Post post);
    boolean deletePost(Long id);
}
