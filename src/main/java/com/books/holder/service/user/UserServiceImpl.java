package com.books.holder.service.user;

import com.books.holder.constants.MessageTemplates;
import com.books.holder.dto.user.UserRequestCreateDto;
import com.books.holder.dto.user.UserRequestFilterDto;
import com.books.holder.dto.user.UserResponseDto;
import com.books.holder.mappers.UserMapper;
import com.books.holder.repository.UserRepository;
import com.books.holder.service.cache.CacheVersionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String CACHE_NAMESPACE = "users";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CacheVersionService cacheVersionService;

    @Override
    @Transactional
    public UserResponseDto saveUser(UserRequestCreateDto userRequestCreateDto) {
        UserResponseDto newUserDto = userMapper.toDto(
                userRepository.save(userMapper.toEntity(userRequestCreateDto)));

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
        return newUserDto;
    }

    @Override
    @Cacheable(value = "userById", key = "#id")
    public UserResponseDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageTemplates.USER_NOT_FOUND_MESSAGE.formatted(id))));
    }

    @Override
    @Cacheable(value = "usersByFilter", key = "{#userRequestFilterDto, @cacheVersionService.getCurrentVersion('users')}")
    public List<UserResponseDto> getUsers(UserRequestFilterDto userRequestFilterDto) {
        return userMapper.mapToDto(
                userRepository.findByFilters(
                        userRequestFilterDto.firstName(), userRequestFilterDto.lastName(), userRequestFilterDto.createdAt())
        );
    }

    @Override
    @CacheEvict(value = "userById", key = "#id")
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
    }

    @Override
    public boolean nicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

}
