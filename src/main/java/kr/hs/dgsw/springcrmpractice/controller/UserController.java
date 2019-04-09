package kr.hs.dgsw.springcrmpractice.controller;

import kr.hs.dgsw.springcrmpractice.domain.User;
import kr.hs.dgsw.springcrmpractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<User> listUsers() {
        return userService.listUsers();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PostMapping("/user")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/user/{id}")
    public boolean deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/user/login")
    public User login(@RequestBody User user) {
        return userService.login(user.getId(), user.getPassword());
    }
}
