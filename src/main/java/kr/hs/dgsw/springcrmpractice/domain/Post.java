package kr.hs.dgsw.springcrmpractice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    private UUID photoFile;

    private String content;

    private String userid;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    public Post(UUID photoFile, String content, String userid) {
        this.photoFile = photoFile;
        this.content = content;
        this.userid = userid;
    }

    public Post(Post post) {
        this.id = post.id;
        this.photoFile = post.photoFile;
        this.content = post.content;
        this.userid = post.userid;
        this.created = post.created;
        this.updated = post.updated;
    }
}
