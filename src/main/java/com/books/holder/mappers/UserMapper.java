package com.books.holder.mappers;

import com.books.holder.dto.user.UserRequestCreateDto;
import com.books.holder.dto.user.UserResponseDto;
import com.books.holder.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestCreateDto userRequestCreateDto);

    UserResponseDto toDto(User user);

    List<UserResponseDto> mapToDto(List<User> users);
}
