package com.th.mymap.user;

import com.th.mymap.response.ApiResponse;
import com.th.mymap.response.ResVo;
import com.th.mymap.user.model.UserCoordinateDto;
import com.th.mymap.user.model.UserSignInDto;
import com.th.mymap.user.model.UserSignInVo;
import com.th.mymap.user.model.UserSignUpDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserSerivce service;

    @PostMapping("/sign-in")
    public ApiResponse<UserSignInVo> postSignIn(HttpServletResponse res, @Valid @RequestBody UserSignInDto dto) {
        return new ApiResponse<>(service.postSignIn(res, dto));
    }

    @PostMapping("/sign-up")
    public ApiResponse<?> postSignUp(@Valid @RequestBody UserSignUpDto dto) {
        return service.postSignUp(dto);
    }

    @PostMapping("/sign-out")
    public ApiResponse<ResVo> postSignout(HttpServletResponse res, HttpServletRequest req) {
        return new ApiResponse<>(service.signout(res, req));
    }

    @GetMapping("/refresh-token")
    public ApiResponse<UserSignInVo> getRefreshToken(HttpServletRequest req) {
        return new ApiResponse<>(service.getRefreshToken(req));
    }

    @GetMapping("/coordinate")
    public ApiResponse<UserCoordinateDto> getCoordinate() {
        return new ApiResponse<>(service.getCoordinate());
    }

    @PatchMapping("/coordinate")
    public ApiResponse<UserCoordinateDto> patchCoordinate(UserCoordinateDto dto) {
        return new ApiResponse<>(service.patchCoordinate(dto));
    }
}
