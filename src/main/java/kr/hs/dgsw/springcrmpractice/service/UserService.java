package kr.hs.dgsw.springcrmpractice.service;

import kr.hs.dgsw.springcrmpractice.domain.User;

import java.util.List;

public interface UserService {
    List<User> listUsers();
    User getUser(String id);
    User addUser(User user);
    User updateUser(String id, User user);
    boolean deleteUser(String id);
    User login(String id, String password);
}
