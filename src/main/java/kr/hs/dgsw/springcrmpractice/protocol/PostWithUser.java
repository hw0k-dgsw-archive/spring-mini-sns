package kr.hs.dgsw.springcrmpractice.protocol;

import kr.hs.dgsw.springcrmpractice.domain.Post;
import kr.hs.dgsw.springcrmpractice.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostWithUser extends Post {
    private String username;

    public PostWithUser(Post post, String username) {
        super(post);
        this.username = username;
    }

    public PostWithUser(Post post, User user) {
        super(post);
        this.username = user.getUsername();
    }

    public PostWithUser(Post post) {
        super(post);
    }
}
