package com.beatstore.userservice.controller.internal;

import com.beatstore.userservice.dto.UserInfoDTO;
import com.beatstore.userservice.service.UserInfoService;
import com.beatstore.userservice.utils.CollectionWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/internal/user")
public class UserInfoInternalController {

    private final UserInfoService userInfoService;

    public UserInfoInternalController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping
    public ResponseEntity<CollectionWrapper<UserInfoDTO>> getUserInfo(@RequestBody Set<String> userHashes) {
        Set<UserInfoDTO> usersInfo = userInfoService.getUsersInfoByUserHashesInternal(userHashes);
        return ResponseEntity.ok(CollectionWrapper.of(usersInfo));
    }
}
