package com.books.holder.validation.validators;

import com.books.holder.service.user.UserService;
import com.books.holder.validation.annotations.UniqueNickname;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueNicknameValidator implements ConstraintValidator<UniqueNickname, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext constraintValidatorContext) {
        return !userService.nicknameExists(nickname);
    }

}
