package com.beatstore.userservice.service;

import com.beatstore.userservice.dto.UserInfoDTO;
import com.beatstore.userservice.model.UsersProfile;
import com.beatstore.userservice.repository.UsersProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInfoService {

    private final UsersProfileRepository usersProfileRepository;

    public UserInfoService(UsersProfileRepository usersProfileRepository) {
        this.usersProfileRepository = usersProfileRepository;
    }


    public Set<UserInfoDTO> getUsersInfoByUserHashesInternal(Set<String> userHashes) {
        List<UsersProfile> users = usersProfileRepository.findAllByUserHashIn(userHashes);
        return users.stream()
                .map(user ->
                        UserInfoDTO.builder()
                                .userHash(user.getUserHash())
                                .displayName(user.getDisplayName())
                                .build())
                .collect(Collectors.toSet());
    }
}
