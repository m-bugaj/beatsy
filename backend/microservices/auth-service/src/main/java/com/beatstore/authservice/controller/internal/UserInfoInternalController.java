package com.beatstore.authservice.controller.internal;

import com.beatstore.authservice.dto.UserInfoDTO;
import com.beatstore.authservice.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/internal/user")
public class UserInfoInternalController {

    private final UserInfoService userInfoService;

    public UserInfoInternalController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping
    public ResponseEntity<Set<UserInfoDTO>> getUserInfo(@RequestBody Set<String> userHashes) {
        Set<UserInfoDTO> usersInfo = userInfoService.getUsersInfoByUserHashes(userHashes);
        return ResponseEntity.ok(usersInfo);
    }
}
