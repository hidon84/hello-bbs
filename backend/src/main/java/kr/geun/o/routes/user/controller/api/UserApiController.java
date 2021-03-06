package kr.geun.o.routes.user.controller.api;

import kr.geun.o.app.user.service.UserApiService;
import kr.geun.o.core.exception.BaseException;
import kr.geun.o.core.response.ResData;
import kr.geun.o.core.utils.CmnUtils;
import kr.geun.o.routes.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 유저 관련 API 컨트롤러
 *
 * @author akageun
 */
@Slf4j
@RestController
@RequestMapping("/api/user/v1")
public class UserApiController {

    @Autowired
    private UserApiService userApiService;

    /**
     * 로그인 API
     *
     * @param param
     * @param result
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ResData> userLogin(
            @Valid UserDTO.Login param,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new BaseException(CmnUtils.getErrMsg(result, '\n'), HttpStatus.BAD_REQUEST);
        }

        try {
            UserDetails userDetails = userApiService.getUserDetails(param.getUserId(), param.getPassWd());

            String token = userApiService.generatorToken(userDetails);

            return ResponseEntity.ok().body(ResData.of(token, "성공"));

        } catch (Exception e) { //TODO : 익셉션 쪼개서 처리해야함.
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResData.of("에러발생"));
        }

    }

    /**
     * 회원가입
     *
     * @param param
     * @param result
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<ResData> signUp(
            @Valid UserDTO.SignUp param,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            throw new BaseException(CmnUtils.getErrMsg(result, '\n'), HttpStatus.BAD_REQUEST);
        }

        try {
            userApiService.preCreateUser(param.getUserId(), param.getPassWd(), param.getConfirmPassWd());
            userApiService.createUser(param.getUserId(), param.getPassWd());

            return ResponseEntity.status(HttpStatus.CREATED).body(ResData.of("성공"));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResData.of("에러발생"));
        }
    }
}
