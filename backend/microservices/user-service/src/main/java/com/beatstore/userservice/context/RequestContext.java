package com.beatstore.userservice.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Setter
@Getter
@Component
@RequestScope
public class RequestContext {
    private String username;
    private String userHash;
    private String subscriptionHash;
    private String role;

}
