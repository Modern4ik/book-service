package com.books.holder.unit.service;

import com.books.holder.dto.user.UserRequestCreateDto;
import com.books.holder.dto.user.UserRequestFilterDto;
import com.books.holder.dto.user.UserResponseDto;
import com.books.holder.entity.User;
import com.books.holder.mappers.UserMapperImpl;
import com.books.holder.repository.UserRepository;
import com.books.holder.service.cache.CacheVersionService;
import com.books.holder.service.user.UserServiceImpl;
import com.books.holder.utils.UserTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private CacheVersionService cacheVersionService;
    @Spy
    private UserMapperImpl userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldSaveUser() {
        UserRequestCreateDto createDto =
                UserTestUtils.generateUserCreateDto(
                        "Test user", "Serg", "Zayts", "test@mail.ru");
        User expectedNewUser =
                UserTestUtils.generateUser(
                        1L, createDto.nickname(), createDto.firstName(), createDto.lastName(), createDto.email(), LocalDateTime.now());

        Mockito.when(userMapper.toEntity(createDto))
                .thenReturn(expectedNewUser);
        Mockito.when(userRepository.save(expectedNewUser))
                .thenReturn(expectedNewUser);

        UserResponseDto responseDto = userService.saveUser(createDto);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(expectedNewUser.getId(), responseDto.getId());
        Assertions.assertEquals(expectedNewUser.getNickname(), responseDto.getNickname());
        Assertions.assertEquals(expectedNewUser.getEmail(), responseDto.getEmail());
        Assertions.assertEquals(expectedNewUser.getCreatedAt(), responseDto.getCreatedAt());
    }

    @Test
    public void shouldReturnUserById() {
        User expectedUser =
                UserTestUtils.generateUser(
                        1L, "Test user", "Serg", "Zayts", "test@mail.ru", LocalDateTime.now());

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        UserResponseDto responseDto = userService.getUserById(1L);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(expectedUser.getId(), responseDto.getId());
        Assertions.assertEquals(expectedUser.getNickname(), responseDto.getNickname());
    }

    @Test
    public void shouldReturnUsersByFirstNameAndCreatedAt() {
        UserRequestFilterDto filterDto =
                UserTestUtils.generateUserFilterDto("Sergey", null, LocalDateTime.now());
        List<User> expectedUsers = UserTestUtils.generateUsersList(filterDto, 3);

        Mockito.when(userRepository.findByFilters(filterDto.firstName(), filterDto.lastName(), filterDto.createdAt()))
                .thenReturn(expectedUsers);

        List<UserResponseDto> responseDtoList = userService.getUsers(filterDto);
        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(3, responseDtoList.size());
        for (UserResponseDto responseDto : responseDtoList) {
            Assertions.assertEquals(filterDto.firstName(), responseDto.getFirstName());
            Assertions.assertEquals(filterDto.createdAt(), responseDto.getCreatedAt());
        }
    }

    @Test
    public void shouldDeleteUserById() {
        userService.deleteUserById(1L);

        Mockito.verify(userRepository).deleteById(1L);
    }
}
