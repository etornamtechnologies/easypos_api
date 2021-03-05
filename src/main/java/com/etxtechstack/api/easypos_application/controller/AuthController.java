package com.etxtechstack.api.easypos_application.controller;

import com.etxtechstack.api.easypos_application.configs.ConfigParams;
import com.etxtechstack.api.easypos_application.models.LoginHistory;
import com.etxtechstack.api.easypos_application.models.User;
import com.etxtechstack.api.easypos_application.repositories.UserRepository;
import com.etxtechstack.api.easypos_application.services.LoginHistoryService;
import com.etxtechstack.api.easypos_application.services.UserService;
import com.etxtechstack.api.easypos_application.utils.CommonHelper;
import com.etxtechstack.api.easypos_application.utils.CommonResponse;
import com.etxtechstack.api.easypos_application.utils.JwtUtil;
import com.etxtechstack.api.easypos_application.vo.GeneralResponse;
import com.etxtechstack.api.easypos_application.vo.LoginRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "etx/easypos/api/v1")
@Slf4j
@AllArgsConstructor
public class AuthController {
    private UserService userService;
    private UserRepository userRepository;
    private LoginHistoryService loginHistoryService;
    private ConfigParams configParams;
    private JwtUtil jwtUtil;

    @Transactional
    @PostMapping(value = "/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse loginUser(@Valid @RequestBody(required = true) LoginRequest req,
                              HttpServletRequest httpServletRequest) {
        log.info("================REQBODY: " + req.toString());
        log.info("=======In Login Controller=========");
        LoginHistory history = new LoginHistory();
        history.setSourceIp(httpServletRequest.getRemoteAddr());
        history.setDestinationIp(httpServletRequest.getLocalAddr());
        history.setStatus("N");
        Optional<User> user = userRepository.findUserByUsernameIgnoreCase(req.getUsername());
        User userVo = new User();
        Map myData = new HashMap();
        if(!user.isPresent()) {
            log.info("User Not Present");
            LoginHistory loginHistory = loginHistoryService.createLoginHistory(history); //save login history
            return new GeneralResponse(CommonResponse.LOGIN_FAILED_CODE, CommonResponse.LOGIN_FAILED_MSG);
        } else {
            User userModel = userService.getUserById(user.get().getId());
            history.setUser(user.get());
            if(user.get().getFailedLoginCount() >= configParams.getFailedLoginCount()
                    || !user.get().getStatus().equals("Y")) {
                log.info("User Is disabled / Inactive");
                userModel.setFailedLoginCount(user.get().getFailedLoginCount() + 1);
                if(user.get().getFailedLoginCount() >= configParams.getFailedLoginCount()) {
                    userModel.setStatus("N");
                }
                history.setStatus("N");
                history.setReason("USER INACTIVE");
                //lets update user's failedLoginCount and status
                userService.updateUser(userModel);
                //lets save log
                loginHistoryService.createLoginHistory(history);
                LoginHistory loginHistory = loginHistoryService.createLoginHistory(history); //save login history
                return new GeneralResponse(CommonResponse.USER_DIABLED_CODE, CommonResponse.USER_DISABLED_MSG);
            } else {
                if(CommonHelper.MatchBCryptPassword(user.get().getPassword(), req.getPassword())) {
                    log.info("User Login Successful");
                    history.setStatus("Y");
                    history.setReason(CommonResponse.SUCCESS_MSG);
                    String token = jwtUtil.GenerateUserToken(user.get());
                    userVo.setId(user.get().getId());
                    userVo.setName(user.get().getName());
                    userVo.setUsername(user.get().getUsername());
                    userVo.setEmail(user.get().getEmail());
                    userVo.setRole(user.get().getRole());
                    userVo.setPhone(user.get().getPhone());
                    userVo.setIsDefaultPassword(user.get().getIsDefaultPassword());
                    myData.put("user", userVo);
                    myData.put("token", token);
                    GeneralResponse response = new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, myData);
                    userModel.setFailedLoginCount(0);
                    userModel.setStatus("Y");
                    //lets update user
                    userService.updateUser(userModel);
                    LoginHistory loginHistory = loginHistoryService.createLoginHistory(history); //save login history
                    return response;
                } else {
                    history.setStatus("N");
                    history.setReason(CommonResponse.LOGIN_FAILED_MSG);
                    GeneralResponse response = new GeneralResponse(CommonResponse.LOGIN_FAILED_CODE, CommonResponse.LOGIN_FAILED_MSG);
                    userModel.setFailedLoginCount(user.get().getFailedLoginCount() + 1);
                    if(user.get().getFailedLoginCount() >= configParams.getFailedLoginCount()) {
                        userModel.setStatus("N");
                    }
                    //lets update user
                    userService.updateUser(userModel);
                    LoginHistory loginHistory = loginHistoryService.createLoginHistory(history); //save login history
                    return  response;
                }
            }
        }
    }
}
