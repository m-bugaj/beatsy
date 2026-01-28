package com.beatstore.marketplaceservice.client;

import com.beatstore.marketplaceservice.dto.UserInfoDTO;
import com.beatstore.marketplaceservice.utils.CollectionWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Set;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/internal/user")
    CollectionWrapper<UserInfoDTO> getUserInfo(@RequestBody Set<String> userHashes);
}
