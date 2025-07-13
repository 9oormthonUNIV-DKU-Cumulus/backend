package com.cumulus.backend.user.service;

import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.mypage.dto.UserInfoDto;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long userId){
        return userRepository.findOne(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public UserInfoDto getUserInfo(Long userId) {
        User user = findById(userId);
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .major(user.getMajor())
                .phoneNumber(user.getPhoneNumber())
                .build();
        return userInfoDto;
    }
}
