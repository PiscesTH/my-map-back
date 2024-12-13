package com.th.mymap.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFacade {
    public MyUserDetails getLoginUser() {
        if ("anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            return null;
        }
        return (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public long getLoginUserPk() {
        if (getLoginUser() == null) {
            return 0;
        }
        return getLoginUser().getMyPrincipal().getIuser();
    }
}
