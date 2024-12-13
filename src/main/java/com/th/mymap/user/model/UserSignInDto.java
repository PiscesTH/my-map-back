package com.th.mymap.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.th.mymap.common.Const.*;

@Data
public class UserSignInDto {
    @Schema(title = "아이디")
    @NotBlank(message = NM_IS_BLANK)
    private String uid;
    @Schema(title = "비밀번호")
    @NotBlank(message = PASSWORD_IS_BLANK)
    private String upw;
}
