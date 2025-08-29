package com.example.coderabbittest.model;

/*
 Test library/framework: JUnit 5 (org.junit.jupiter)
 Generated: August 29, 2025
 Notes:
 - Focused on validating the public API of the User POJO (constructor, getters, setters).
 - Covers happy paths, nulls/empties, special characters, and Long boundary values.
*/

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    @DisplayName("Constructor sets all fields and getters return the supplied values")
    void constructorAndGettersReturnSuppliedValues() {
        User u = new User(42L, "Alice", "alice@example.com", "secret");
        assertAll("constructor sets fields",
                () -> assertEquals(Long.valueOf(42L), u.getId()),
                () -> assertEquals("Alice", u.getName()),
                () -> assertEquals("alice@example.com", u.getEmail()),
                () -> assertEquals("secret", u.getPassword())
        );
    }

    @Test
    @DisplayName("Setters update fields correctly")
    void settersUpdateFieldsCorrectly() {
        User u = new User(null, null, null, null);
        u.setId(10L);
        u.setName("Bob");
        u.setEmail("bob@example.com");
        u.setPassword("p@ss");

        assertAll("setters update fields",
                () -> assertEquals(Long.valueOf(10L), u.getId()),
                () -> assertEquals("Bob", u.getName()),
                () -> assertEquals("bob@example.com", u.getEmail()),
                () -> assertEquals("p@ss", u.getPassword())
        );
    }

    @Test
    @DisplayName("Setters accept null and empty string values without throwing")
    void settersAllowNullAndEmptyStrings() {
        User u = new User(1L, "X", "Y", "Z");

        // Nulls
        u.setName(null);
        u.setEmail(null);
        u.setPassword(null);
        assertAll("nulls accepted",
                () -> assertNull(u.getName()),
                () -> assertNull(u.getEmail()),
                () -> assertNull(u.getPassword())
        );

        // Empties
        u.setName("");
        u.setEmail("");
        u.setPassword("");
        assertAll("empty strings accepted",
                () -> assertEquals("", u.getName()),
                () -> assertEquals("", u.getEmail()),
                () -> assertEquals("", u.getPassword())
        );
    }

    @Test
    @DisplayName("Password is stored exactly as provided (including whitespace, symbols, unicode)")
    void passwordIsStoredExactly() {
        String pw = "  P@$$ w0rd \t\n😊";
        User u = new User(2L, "A", "B", pw);
        assertEquals(pw, u.getPassword(), "Password should be stored verbatim");

        String newPw = "\n\t another🔒 one ";
        u.setPassword(newPw);
        assertEquals(newPw, u.getPassword(), "Updated password should also be stored verbatim");
    }

    @Test
    @DisplayName("ID handles Long boundary values")
    void idBoundaryValues() {
        User u = new User(Long.MIN_VALUE, "A", "B", "C");
        assertEquals(Long.valueOf(Long.MIN_VALUE), u.getId());

        u.setId(Long.MAX_VALUE);
        assertEquals(Long.valueOf(Long.MAX_VALUE), u.getId());
    }
}