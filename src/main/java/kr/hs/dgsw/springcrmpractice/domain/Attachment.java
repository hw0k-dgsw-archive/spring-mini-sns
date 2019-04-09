package kr.hs.dgsw.springcrmpractice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    @Id
    private UUID id;

    private String path;
    private String origin;

    @CreationTimestamp
    private Timestamp created;

    public Attachment(UUID id, String path, String origin) {
        this.id = id;
        this.path = path;
        this.origin = origin;
    }
}
