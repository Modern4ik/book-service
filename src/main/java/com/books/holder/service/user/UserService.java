package com.books.holder.service.user;

import com.books.holder.dto.user.UserRequestCreateDto;
import com.books.holder.dto.user.UserRequestFilterDto;
import com.books.holder.dto.user.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto saveUser(UserRequestCreateDto userRequestCreateDto);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getUsers(UserRequestFilterDto userRequestFilterDto);

    void deleteUserById(Long id);

    boolean nicknameExists(String nickname);

    boolean emailExists(String email);
}
