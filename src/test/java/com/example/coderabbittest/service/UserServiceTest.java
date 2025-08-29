// Testing Framework: JUnit 5 (Jupiter) via spring-boot-starter-test. No new dependencies introduced.
package com.example.coderabbittest.service;

import com.example.coderabbittest.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService();
    }

    // getAllUsers

    @Test
    void getAllUsers_returnsTwoSeedUsers_andIsMutable() {
        List<User> all = service.getAllUsers();
        assertNotNull(all, "getAllUsers should never return null");
        assertEquals(2, all.size(), "Seed data should contain exactly two users");

        assertTrue(all.stream().anyMatch(u -> "Alice".equals(u.getName())), "Seed should include Alice");
        assertTrue(all.stream().anyMatch(u -> "Bob".equals(u.getName())), "Seed should include Bob");

        // Document current behavior: returned list is the internal list (mutable/leaky)
        all.add(new User(99L, "Eve", "eve@example.com", "pw"));
        assertEquals(3, service.getAllUsers().size(), "Mutating returned list should reflect in service state");
    }

    // getUserById

    @Test
    void getUserById_existingIds_returnUser() {
        User u1 = service.getUserById(1L);
        assertNotNull(u1);
        assertEquals(Long.valueOf(1L), u1.getId());
        assertEquals("Alice", u1.getName());

        User u2 = service.getUserById(2L);
        assertNotNull(u2);
        assertEquals(Long.valueOf(2L), u2.getId());
        assertEquals("Bob", u2.getName());
    }

    @Test
    void getUserById_unknownId_returnsNull() {
        assertNull(service.getUserById(0L));
        assertNull(service.getUserById(999L));
    }

    @Test
    void getUserById_null_returnsNull() {
        assertNull(service.getUserById(null));
    }

    // findUsersByName (currently case-sensitive)

    @Test
    void findUsersByName_caseSensitive_partialMatch_returnsMatches() {
        List<User> res = service.findUsersByName("Ali");
        assertEquals(1, res.size());
        assertEquals("Alice", res.get(0).getName());
    }

    @Test
    void findUsersByName_caseSensitive_mismatchedCase_returnsEmpty() {
        List<User> res = service.findUsersByName("alice");
        assertTrue(res.isEmpty(), "Case-mismatched search should currently yield no results");
    }

    @Test
    void findUsersByName_noMatch_returnsEmpty() {
        List<User> res = service.findUsersByName("Charlie");
        assertTrue(res.isEmpty());
    }

    @Test
    void findUsersByName_emptyQuery_returnsAllUsersContainingEmptyString() {
        List<User> res = service.findUsersByName("");
        assertEquals(2, res.size(), "Empty substring should match all users");
    }

    @Test
    void findUsersByName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findUsersByName(null),
                "Current implementation should throw NPE when name is null");
    }

    // updateUser

    @Test
    void updateUser_existing_replacesUser() {
        User updated = new User(1L, "AliceUpdated", "alice@new.example.com", "newpass");
        service.updateUser(updated);
        User fetched = service.getUserById(1L);
        assertNotNull(fetched);
        assertEquals("AliceUpdated", fetched.getName());
        assertEquals("alice@new.example.com", fetched.getEmail());
        assertEquals("newpass", fetched.getPassword());
    }

    @Test
    void updateUser_nonExisting_doesNothing() {
        int before = service.getAllUsers().size();
        service.updateUser(new User(123L, "Ghost", "ghost@example.com", "none"));
        assertEquals(before, service.getAllUsers().size());
        assertNull(service.getUserById(123L));
    }

    @Test
    void updateUser_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.updateUser(null));
    }

    // deleteUser

    @Test
    void deleteUser_existingId_removesUser() {
        int before = service.getAllUsers().size();
        service.deleteUser(2L);
        assertEquals(before - 1, service.getAllUsers().size());
        assertNull(service.getUserById(2L));
    }

    @Test
    void deleteUser_unknownId_noChange() {
        int before = service.getAllUsers().size();
        service.deleteUser(999L);
        assertEquals(before, service.getAllUsers().size());
    }

    @Test
    void deleteUser_nullId_noChange() {
        int before = service.getAllUsers().size();
        service.deleteUser(null);
        assertEquals(before, service.getAllUsers().size());
    }

    @Test
    void deleteUser_swallowedException_doesNotPropagate_andNoMutationWhenPredicateThrows() {
        // Add a user with null id so user.getId().equals(id) triggers NPE in the predicate
        service.getAllUsers().add(new User(null, "NullIdUser", "null@ex.com", "pw"));
        int before = service.getAllUsers().size();

        // Should swallow the exception internally
        service.deleteUser(123456L);

        // Size unchanged; original entries remain
        assertEquals(before, service.getAllUsers().size());
        assertNotNull(service.getUserById(1L));
    }
}