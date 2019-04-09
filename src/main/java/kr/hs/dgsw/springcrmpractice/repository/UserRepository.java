package kr.hs.dgsw.springcrmpractice.repository;

import kr.hs.dgsw.springcrmpractice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdAndPassword(String id, String password);
    Optional<User> findByEmail(String email);
}
