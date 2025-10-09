package com.beatstore.marketplaceservice.client;

import com.beatstore.marketplaceservice.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Set;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/internal/user")
    Set<UserInfoDTO> getUserInfo(@RequestBody Set<String> userHashes);
}
