package com.books.holder.mappers;

import com.books.holder.dto.user.UserRequestCreateDto;
import com.books.holder.dto.user.UserResponseDto;
import com.books.holder.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(UserRequestCreateDto userRequestCreateDto);

    UserResponseDto toDto(User user);

    List<UserResponseDto> mapToDto(List<User> users);
}
