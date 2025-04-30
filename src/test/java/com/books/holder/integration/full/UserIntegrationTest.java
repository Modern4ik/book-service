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

import java.time.LocalDate;
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
                        "Nickname", null, null, "test@mail.ru", LocalDate.of(2022, 4, 5));

        UserResponseDto responseDto = userController.saveUser(createDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(4L, responseDto.id());
        Assertions.assertEquals(createDto.nickname(), responseDto.nickname());
        Assertions.assertEquals(createDto.email(), responseDto.email());
        Assertions.assertEquals(createDto.registrationDate(), responseDto.registrationDate());

        Assertions.assertEquals(4, userRepository.count());
    }

    @Test
    public void shouldReturnUserById() {
        UserResponseDto responseDto = userController.getUserById(2L);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(2L, responseDto.id());
        Assertions.assertEquals("TWO", responseDto.nickname());
    }

    @Test
    public void shouldReturnUsersByFirstNameAndRegistrationDate() {
        UserRequestFilterDto filterDto =
                UserTestUtils.generateUserFilterDto("Serg", null, LocalDate.of(2020, 3, 5));

        List<UserResponseDto> responseDtoList = userController.getUsers(filterDto);

        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(2, responseDtoList.size());
        for (UserResponseDto responseDto : responseDtoList) {
            Assertions.assertEquals(filterDto.firstName(), responseDto.firstName());
            Assertions.assertEquals(filterDto.registrationDate(), responseDto.registrationDate());
        }
    }

    @Test
    public void shouldDeleteUserById() {
        userController.deleteUserById(1L);

        Assertions.assertFalse(userRepository.existsById(1L));
        Assertions.assertEquals(2, userRepository.count());
    }

}
