package kr.hs.dgsw.springcrmpractice.service;

import kr.hs.dgsw.springcrmpractice.domain.Post;
import kr.hs.dgsw.springcrmpractice.protocol.PostWithUser;
import kr.hs.dgsw.springcrmpractice.repository.PostRepository;
import kr.hs.dgsw.springcrmpractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class PostWithUserServiceImpl implements PostWithUserService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostWithUserServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    private PostWithUser convertToPostWithUser(Post post) {
        return new PostWithUser(
                post,
                Objects.requireNonNull(userRepository.findById(post.getUserid()).orElse(null))
        );
    }

    @Override
    public List<PostWithUser> listPosts() {
        List<Post> postList = postRepository.findAll();

        List<PostWithUser> postWithUserList = new ArrayList<>();
        postList.forEach(post -> postWithUserList.add(convertToPostWithUser(post)));

        return postWithUserList;
    }

    @Override
    public PostWithUser getPost(Long id) {
        return convertToPostWithUser(postRepository.findById(id).orElse(null));
    }

    @Override
    public PostWithUser addPost(Post post) {
        return convertToPostWithUser(postRepository.save(post));
    }

    @Override
    public PostWithUser updatePost(Long id, Post post) {
        Post target;
        try {
            target = postRepository.findById(id).orElseThrow(() -> null);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

        target.setPhotoFile(post.getPhotoFile() != null ? post.getPhotoFile() : target.getPhotoFile());
        target.setContent(post.getContent() != null ? post.getContent() : target.getContent());
        target.setUserid(post.getUserid() != null ? post.getUserid() : target.getUserid());

        return convertToPostWithUser(postRepository.save(target));
    }

    @Override
    public boolean deletePost(Long id) {
        Optional<Post> target = postRepository.findById(id);
        if (!target.isPresent()) {
            return false;
        }

        try {
            postRepository.deleteById(id);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
