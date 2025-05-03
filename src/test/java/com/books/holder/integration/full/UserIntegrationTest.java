package com.books.holder.integration.full;

import com.books.holder.controller.UserController;
import com.books.holder.dto.user.UserRequestCreateDto;
import com.books.holder.dto.user.UserRequestFilterDto;
import com.books.holder.dto.user.UserResponseDto;
import com.books.holder.repository.UserRepository;
import com.books.holder.utils.UserTestUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/test-users-data.sql")
public class UserIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void resetSequence() {
        entityManager.createNativeQuery(
                "ALTER TABLE users ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();
    }

    @Test
    public void shouldSaveUser() {
        UserRequestCreateDto createDto =
                UserTestUtils.generateUserCreateDto(
                        "Nickname", null, null, "test@mail.ru");

        UserResponseDto responseDto = userController.saveUser(createDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(4L, responseDto.getId());
        Assertions.assertEquals(createDto.nickname(), responseDto.getNickname());
        Assertions.assertEquals(createDto.email(), responseDto.getEmail());
        Assertions.assertNotNull(responseDto.getCreatedAt());

        Assertions.assertEquals(4, userRepository.count());
    }

    @Test
    public void shouldReturnUserById() {
        UserResponseDto responseDto = userController.getUserById(2L);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(2L, responseDto.getId());
        Assertions.assertEquals("TWO", responseDto.getNickname());
    }

    @Test
    public void shouldReturnUsersByFirstNameAndLastName() {
        UserRequestFilterDto filterDto =
                UserTestUtils.generateUserFilterDto("Serg", "Zayts", null);

        List<UserResponseDto> responseDtoList = userController.getUsers(filterDto);

        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(2, responseDtoList.size());
        for (UserResponseDto responseDto : responseDtoList) {
            Assertions.assertEquals(filterDto.firstName(), responseDto.getFirstName());
            Assertions.assertEquals(filterDto.lastName(), responseDto.getLastName());
        }
    }

    @Test
    public void shouldDeleteUserById() {
        userController.deleteUserById(1L);

        Assertions.assertFalse(userRepository.existsById(1L));
        Assertions.assertEquals(2, userRepository.count());
    }

}
