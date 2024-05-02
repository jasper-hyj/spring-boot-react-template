package com.example;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.exception.request.NotFoundException;
import com.example.service.user.UserService;
import com.example.service.user.model.User;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
class SpringBootReactApplicationTests {
    private static String TEST_USER_ID_STRING = "";

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    void setup() throws NotFoundException {
        this.user = userService.getUserById(UUID.fromString(TEST_USER_ID_STRING));
    }

    @Test
    void loadUser() {
        assertNotNull(this.user);
        log.info(this.user);
    }
}
