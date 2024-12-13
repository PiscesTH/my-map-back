package com.th.mymap.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignInVo {
    @Schema(title = "access token")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String accessToken;
}
