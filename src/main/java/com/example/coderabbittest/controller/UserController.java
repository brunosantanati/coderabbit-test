package com.example.coderabbittest.controller;

import com.example.coderabbittest.model.User;
import com.example.coderabbittest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Issue: Exposing sensitive data. The password should not be returned.
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Issue: Insecure Direct Object Reference (IDOR).
    // Any user can access any other user's data by just knowing their ID.
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            // Issue: Redundant database call. The user is fetched twice.
            return ResponseEntity.ok(userService.getUserById(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Issue: Using GET for a state-changing operation. This should be a POST or PUT.
    @GetMapping("/updateEmail")
    public ResponseEntity<String> updateUserEmail(@RequestParam Long userId, @RequestParam String newEmail) {
        try {
            User user = userService.getUserById(userId);
            user.setEmail(newEmail);
            userService.updateUser(user);
            return ResponseEntity.ok("User email updated");
        } catch (Exception e) {
            // Issue: Poor error handling. Returning a generic 500 error for all exceptions.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}
