package com.beatstore.userservice.service;

import com.beatstore.userservice.dto.UserInfoDTO;
import com.beatstore.userservice.model.UserAccount;
import com.beatstore.userservice.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInfoService {

    private final UserAccountRepository userAccountRepository;

    public UserInfoService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public Set<UserInfoDTO> getUsersInfoByUserHashes(Set<String> userHashes) {
        List<UserAccount> users = userAccountRepository.findAllByUserHashIn(userHashes);
        return users.stream()
                .map(user ->
                        UserInfoDTO.builder()
                                .userHash(user.getUserHash())
                                .username(user.getUsername())
                                .build())
                .collect(Collectors.toSet());
    }
}
