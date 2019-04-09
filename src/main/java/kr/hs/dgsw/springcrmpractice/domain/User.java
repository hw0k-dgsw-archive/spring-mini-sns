package kr.hs.dgsw.springcrmpractice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;

    private UUID avatarFile;

    private String username;

    private String email;

    private String password;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    public User(String id, UUID avatarFile, String username, String email, String password) {
        this.id = id;
        this.avatarFile = avatarFile;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
