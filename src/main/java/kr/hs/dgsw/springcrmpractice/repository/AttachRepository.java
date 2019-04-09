package kr.hs.dgsw.springcrmpractice.repository;

import kr.hs.dgsw.springcrmpractice.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AttachRepository extends JpaRepository<Attachment, Long> {
    Optional<Attachment> findById(UUID id);
}
