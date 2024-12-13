package com.th.mymap.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.th.mymap.common.Const.*;


@Data
@Schema(title = "회원가입 시 필요한 사용자 데이터")
public class UserSignUpDto {
    @JsonIgnore
    private int iuser;

    @Schema(description = "유저 아이디", example = "Test1234")
    @NotNull(message = ID_IS_BLANK)
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{6,10}$",
            message = NOT_ALLOWED_ID)
    private String uid;

    @Schema(description = "유저 비밀번호", example = "특수문자는 @$!%*?&#~_-를 사용할 수 있습니다.")
    @NotNull(message = PASSWORD_IS_BLANK)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#~_-])[A-Za-z\\d@$!%*?&#.~_-]{8,20}$",
            message = NOT_ALLOWED_PASSWORD)
    private String upw;

    @Schema(title = "이름")
    @NotBlank(message = NM_IS_BLANK)
    private String nm;

    @Schema(title = "이메일")
    @NotNull(message = EMAIL_IS_BLANK)
    @Pattern(regexp = "\\w+@\\w{3,}\\.([a-zA-Z]{2,}|[a-zA-Z]{2,}\\.[a-zA-Z]{2,})",
            message = NOT_ALLOWED_EMAIL)
    private String email;
}