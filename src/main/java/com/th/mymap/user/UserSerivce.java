package com.th.mymap.user;

import com.th.mymap.common.AppProperties;
import com.th.mymap.common.Const;
import com.th.mymap.common.MyCookieUtils;
import com.th.mymap.entity.User;
import com.th.mymap.exception.AuthErrorCode;
import com.th.mymap.exception.CommonErrorCode;
import com.th.mymap.exception.RestApiException;
import com.th.mymap.repository.UserRepository;
import com.th.mymap.response.ApiResponse;
import com.th.mymap.response.ResVo;
import com.th.mymap.security.AuthenticationFacade;
import com.th.mymap.security.JwtTokenProvider;
import com.th.mymap.security.MyPrincipal;
import com.th.mymap.security.MyUserDetails;
import com.th.mymap.user.model.UserCoordinateDto;
import com.th.mymap.user.model.UserSignInDto;
import com.th.mymap.user.model.UserSignInVo;
import com.th.mymap.user.model.UserSignUpDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.th.mymap.common.Const.rtName;
import static com.th.mymap.exception.AuthErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSerivce {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final MyCookieUtils myCookieUtils;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public ApiResponse<?> postSignUp(UserSignUpDto dto) {
        Optional<User> uidCheck = userRepository.findByUid(dto.getUid());
        if (uidCheck.isPresent()) {
            throw new RestApiException(DUPLICATED_UID);
        }
        Optional<User> emailCheck = userRepository.findByEmail(dto.getEmail());
        if (emailCheck.isPresent()) {
            throw new RestApiException(DUPLICATED_EMAIL);
        }
        String hashedUpw = passwordEncoder.encode(dto.getUpw());
        User user = new User();
        user.setNm(dto.getNm());
        user.setUpw(hashedUpw);
        user.setUid(dto.getUid());
        user.setEmail(dto.getEmail());

        userRepository.save(user);

        return new ApiResponse<>(null);
    }

    //로그인
    public UserSignInVo postSignIn(HttpServletResponse res, UserSignInDto dto) {
        Optional<User> optEntity = userRepository.findByUid(dto.getUid());
        User entity = optEntity.orElseThrow(() -> new RestApiException(AuthErrorCode.LOGIN_FAIL));
        if (!passwordEncoder.matches(dto.getUpw(), entity.getUpw())) {
            throw new RestApiException(AuthErrorCode.LOGIN_FAIL);
        }

        MyPrincipal myPrincipal = MyPrincipal.builder()
                .iuser(entity.getIuser())
                .build();

        String at = jwtTokenProvider.generateAccessToken(myPrincipal);
        String rt = jwtTokenProvider.generateRefreshToken(myPrincipal);

        int rtCookieMaxAge = appProperties.getJwt().getRefreshCookieMaxAge();
        myCookieUtils.deleteCookie(res, rtName);
        myCookieUtils.setCookie(res, rtName, rt, rtCookieMaxAge);

        return UserSignInVo.builder()
                .accessToken(at)
                .build();
    }

    //로그 아웃
    public ResVo signout(HttpServletResponse res, HttpServletRequest req) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(req, res, SecurityContextHolder.getContext().getAuthentication());
        myCookieUtils.deleteCookie(res, "accessToken");
        myCookieUtils.deleteCookie(res, "refreshToken");
        return new ResVo(Const.SUCCESS);
    }

    //accessToken 재발급
    public UserSignInVo getRefreshToken(HttpServletRequest req) {
        Cookie cookie = myCookieUtils.getCookie(req, rtName);
        if (cookie == null) {
            return UserSignInVo.builder()
                    .accessToken(null)
                    .build();
        }
        String token = cookie.getValue();
        if (!jwtTokenProvider.isValidateToken(token)) {
            return UserSignInVo.builder()
                    .accessToken(null)
                    .build();
        }
        MyUserDetails myUserDetails = (MyUserDetails) jwtTokenProvider.getUserDetailsFromToken(token);
        MyPrincipal myPrincipal = myUserDetails.getMyPrincipal();

        String at = jwtTokenProvider.generateAccessToken(myPrincipal);

        return UserSignInVo.builder()
                .accessToken(at)
                .build();
    }

    public UserCoordinateDto getCoordinate() {
        Optional<User> opt = userRepository.findById(authenticationFacade.getLoginUserPk());
        if (opt.isEmpty()) {
            throw new RestApiException(CommonErrorCode.BAD_REQUEST);
        }
        User user = opt.get();
        UserCoordinateDto result = new UserCoordinateDto();
        result.setLat(user.getLat());
        result.setLng(user.getLng());
        return result;
    }


    public UserCoordinateDto modifyCoordinate(UserCoordinateDto dto) {
        Optional<User> opt = userRepository.findById(authenticationFacade.getLoginUserPk());
        if (opt.isEmpty()) {
            throw new RestApiException(CommonErrorCode.BAD_REQUEST);
        }
        User user = opt.get();
        user.setLng(dto.getLng());
        user.setLat(dto.getLat());
        userRepository.save(user);
        return dto;
    }
}
