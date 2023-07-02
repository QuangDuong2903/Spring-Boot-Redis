package com.quangduong.redis.repository;

import com.quangduong.redis.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser() {

        UserEntity user = new UserEntity();
        user.setUsername("quangduong");
        user.setPassword("292003");

        UserEntity entity = userRepository.save(user);

        assertThat(entity).usingRecursiveComparison().ignoringFields("id").isEqualTo(user);
    }

    @Test
    public void shouldNotSaveUser() {

        UserEntity user = new UserEntity();
        user.setUsername("quangduong");
        user.setPassword("292003");
        userRepository.save(user);

        UserEntity newUser = new UserEntity();
        newUser.setUsername("quangduong");
        newUser.setPassword("15092003");

        Assertions.assertThatExceptionOfType(Exception.class).isThrownBy(() -> userRepository.save(newUser));
    }

    @Test
    public void shouldFindUser() {

        UserEntity user = new UserEntity();
        user.setUsername("quangduong");
        user.setPassword("292003");
        userRepository.save(user);

        Optional<UserEntity> result = userRepository.findOneByUsername("quangduong");

        Assertions.assertThat(result.isEmpty()).isFalse();
        Assertions.assertThat(result.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(user);
    }

    @Test
    public void shouldNotFindUser() {

        UserEntity user = new UserEntity();
        user.setUsername("quangduong");
        user.setPassword("292003");
        userRepository.save(user);

        Optional<UserEntity> result = userRepository.findOneByUsername("quangduonggg");

        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void shouldUpdateUser() {

        UserEntity user = new UserEntity();
        user.setUsername("quangduong");
        user.setPassword("292003");
        userRepository.save(user);

        user.setUsername("quangduong");
        user.setPassword("15092003");
        UserEntity result = userRepository.save(user);

        Assertions.assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(user);
    }

    @Test
    public void shouldDeleteUser() {

        UserEntity user = new UserEntity();
        user.setUsername("quangduong");
        user.setPassword("292003");
        user = userRepository.save(user);

        userRepository.deleteById(user.getId());

        Assertions.assertThat(userRepository.findAll().size()).isEqualTo(0);
    }
}