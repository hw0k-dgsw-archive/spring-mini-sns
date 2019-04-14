package kr.hs.dgsw.springcrmpractice.service;

import kr.hs.dgsw.springcrmpractice.domain.User;
import kr.hs.dgsw.springcrmpractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String DDLMode;

    @PostConstruct
    private void init() {
        if (DDLMode.equals("create") || DDLMode.equals("create-drop")) {
            for (int i = 1; i <= 3; i++) {
                userRepository.save(new User("" + i, null, "" + i, "" + i + "@dgsw.hs.kr", "" + i));
            }
        }
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User addUser(User user) {
        Optional<User> target = userRepository.findByEmail(user.getEmail());
        if (target.isPresent()) {
            return null;
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id, User user) {
        Optional<User> target = userRepository.findById(id);
        if (!target.isPresent()) {
            return null;
        }

        User targetUser = target.get();
        targetUser.setUsername(user.getUsername() != null ? user.getUsername() : targetUser.getUsername());
        targetUser.setEmail(user.getEmail() != null ? user.getEmail() : targetUser.getEmail());
        targetUser.setAvatarFile(user.getAvatarFile() != null ? user.getAvatarFile() : targetUser.getAvatarFile());

        return userRepository.save(targetUser);
    }

    @Override
    public boolean deleteUser(String id) {
        Optional<User> target = userRepository.findById(id);
        if (!target.isPresent()) {
            return false;
        }

        try {
            userRepository.deleteById(id);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public User login(String id, String password) {
        return userRepository.findByIdAndPassword(id, password).orElse(null);
    }


}
