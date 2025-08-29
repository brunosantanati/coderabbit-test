package com.example.coderabbittest.service;

import com.example.coderabbittest.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final List<User> userList = new ArrayList<>();

    public UserService() {
        // Hardcoded values instead of using a database or a configuration file
        userList.add(new User(1L, "Alice", "alice@example.com", "password123"));
        userList.add(new User(2L, "Bob", "bob@example.com", "password456"));
    }

    public List<User> getAllUsers() {
        return userList;
    }

    public User getUserById(Long id) {
        // Inefficient search: This should be a map lookup for better performance
        for (User user : userList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public List<User> findUsersByName(String name) {
        // Logic error: This is doing a case-sensitive search. A case-insensitive search is usually more user-friendly.
        return userList.stream()
                .filter(user -> user.getName().contains(name))
                .collect(Collectors.toList());
    }

    public void updateUser(User userToUpdate) {
        // Potential NullPointerException: userToUpdate can be null and there is no check for it.
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(userToUpdate.getId())) {
                userList.set(i, userToUpdate);
                return;
            }
        }
    }

    public void deleteUser(Long id) {
        try {
            // Ignoring exception: If something goes wrong during removal, the exception is caught but nothing is done about it.
            userList.removeIf(user -> user.getId().equals(id));
        } catch (Exception e) {
            // Ignoring the exception
        }
    }
}
