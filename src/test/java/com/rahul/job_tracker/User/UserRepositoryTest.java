package com.rahul.job_tracker.User;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setEmail("rahi@gmail.com");
        user.setFullName("Rahi Turkar");
        user.setPassword("12345678");
        userRepository.save(user);

        User actualUser = userRepository.findByEmail("rahi@gmail.com").get();

        assertThat(actualUser).isEqualTo(user);
    }
}
