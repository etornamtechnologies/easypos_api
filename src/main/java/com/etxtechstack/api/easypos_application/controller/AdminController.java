package com.etxtechstack.api.easypos_application.controller;


import com.etxtechstack.api.easypos_application.configs.ConfigParams;
import com.etxtechstack.api.easypos_application.models.*;
import com.etxtechstack.api.easypos_application.repositories.UserRepository;
import com.etxtechstack.api.easypos_application.services.*;
import com.etxtechstack.api.easypos_application.utils.*;
import com.etxtechstack.api.easypos_application.vo.*;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/etx-easypos/api/v1")
public class AdminController {
    private Gson gson;

    @Autowired
    private DBValidateHelper dbValidateHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private Logger logger;

    @Autowired
    private ConfigParams configParams;

    @Autowired
    private ActivityLogService activityLogService;

    public AdminController() {
        gson = new Gson();
    }

    //===================================Permissions============================
    @GetMapping(value = "/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getAllPermissions(@RequestHeader(name = "Authorization", required = true) String token) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.PermissionGetAll, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        List<Permission> permissions = permissionService.getAllPermissions();
        Map data = new HashMap();
        data.put("permissions", permissions);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }



    //==============================roles========================


    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getAllRoles(@RequestHeader(name = "Authorization", required = true) String token) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.RoleGetAll, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        List<Role> roles = roleService.getAllRoles();
        Map data = new HashMap();
        data.put("roles", roles);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @GetMapping(value = "/roles/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getRole(@RequestHeader(name = "Authorization", required = true) String token,
                            @PathVariable(name = "roleId") Integer roleId) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.RoleGetAll, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        Role role = roleService.getRoleById(roleId);
        Map data = new HashMap();
        data.put("role", role);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @PostMapping(value = "/roles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse createRole(@RequestHeader(name = "Authorization", required = true) String token,
                               @RequestBody RoleRequestVo reqBody,
                               HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.RoleCreate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        //lets validate form fields
        DBValidationResponse validateRes = dbValidateHelper.validateRole(reqBody.getName());
        if(validateRes.isExists()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, validateRes.getMessage());
        }
        //lets get permissions
        System.out.println("Permision Ids: " + reqBody.getPermissionIds());
        List<Permission> permissions = reqBody.getPermissionIds().stream().map(permissionId-> {
            return permissionService.getPermissionById(permissionId);
        }).collect(Collectors.toList());
        Role roleModel = new Role(reqBody.getName().toUpperCase(), reqBody.getDescription().toUpperCase(), permissions);
        Role createdRole = roleService.createRole(roleModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.CREATE, SystemModels.ROLE, String.valueOf(createdRole.getId()),
                createdRole.getName(), gson.toJson(reqBody), request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        Map data = new HashMap();
        data.put("role", createdRole);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @PutMapping(value = "/roles/{roleId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse updateRole(@RequestHeader(name = "Authorization", required = true) String token,
                               @PathVariable(name = "roleId") Integer roleId,
                               @RequestBody RoleRequestVo reqBody,
                               HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.RoleUpdate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        //lets validate form fields
        DBValidationResponse validateRes = dbValidateHelper.validateRole(reqBody.getName());
        if(validateRes.isExists() && !validateRes.getId().equals(roleId)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, validateRes.getMessage());
        }
        //lets get permissions
        List<Permission> permissions = reqBody.getPermissionIds().stream().map(permissionId-> {
            return permissionService.getPermissionById(permissionId);
        }).collect(Collectors.toList());
        Role roleModel = roleService.getRoleById(roleId);
        roleModel.setName(reqBody.getName().toUpperCase());
        roleModel.setDescription(reqBody.getDescription().toUpperCase());
        roleModel.setPermissions(permissions);
        Role updatedRole = roleService.updateRole(roleModel);

        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.UPDATE, SystemModels.ROLE, String.valueOf(updatedRole.getId()),
                updatedRole.getName(), gson.toJson(reqBody), request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        Map data = new HashMap();
        data.put("role", updatedRole);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @DeleteMapping(value = "/roles/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse deleteRole(@RequestHeader(name = "Authorization", required = true) String token,
                               @PathVariable(name = "roleId") Integer roleId,
                               HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.RoleDelete, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        Role roleModel = roleService.getRoleById(roleId);
        roleService.deleteRole(roleModel);

        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.DELETE, SystemModels.ROLE, String.valueOf(roleModel.getId()),
                roleModel.getName(), null, request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, null);
    }


    //==========================================Users==================================

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getAllUser(@RequestHeader(name = "Authorization", required = true) String token) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.UserGetAll, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        List<User> users = userService.getAllUsers();
        Map data = new HashMap();
        data.put("users", users);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @GetMapping(value = "/user-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getUserInfoByAccessToken(@RequestHeader(name = "Authorization", required = true) String token,
                                             HttpServletRequest request) {
        logger.info("===========Get User Info By Access Token=======");
        //lets authenticate
        System.out.println("TOKEN: " + token);
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        User user = userService.getUserById(authUserId);
        Map data = new HashMap();
        data.put("user", user);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @GetMapping(value = "/users/{user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getUserDetails(@RequestHeader(name = "Authorization", required = true) String token,
                                   @PathVariable(name = "userId") Integer userId,
                                   HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(CommonHelper.HasPermission(Permissions.UserGetAll, authUser.getRole().getPermissions()) ||
                authUser.equals(userId)) {
            User user = userService.getUserById(userId);
            Map data = new HashMap();
            data.put("user", user);
            return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
        }
        return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
    }

    @Transactional
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse createUser(@RequestHeader(name = "Authorization", required = true) String token,
                               @RequestBody UserRequestVo reqBody,
                               HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.UserCreate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        //lets validate form fields
        DBValidationResponse validateRes = dbValidateHelper.validateUser(reqBody.getUsername(), reqBody.getEmail(), reqBody.getPhone());
        if(validateRes.isExists()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, validateRes.getMessage());
        }
        String password = CommonHelper.GeneratePassword(null, 10);
        String hashedPassd = CommonHelper.GenerateBCryptEncoder(password);
        //lets get role
        Role role = roleService.getRoleById(reqBody.getRoleId());
        User userModel = new User(reqBody.getName().toUpperCase(), reqBody.getUsername(), reqBody.getEmail(), reqBody.getPhone(),
                reqBody.getAddress().toUpperCase(), hashedPassd, "Y", role, authUserId);
        User createdUser = userService.createUser(userModel);

        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.CREATE, SystemModels.USER, String.valueOf(createdUser.getId()),
                createdUser.getName(), gson.toJson(reqBody), request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);

        Map data = new HashMap();
        data.put("user", createdUser);
        data.put("password", password);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @PutMapping(value = "/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse updateUser(@RequestHeader(name = "Authorization", required = true) String token,
                               @PathVariable(name = "userId") Integer userId,
                               @RequestBody UserRequestVo reqBody,
                               HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.UserUpdate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        //lets validate form fields
        DBValidationResponse validateRes = dbValidateHelper.validateUser(reqBody.getUsername(), reqBody.getEmail(), reqBody.getPhone());
        if(validateRes.isExists() && !validateRes.getId().equals(userId)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, validateRes.getMessage());
        }
        //lets get permissions
        Role role = roleService.getRoleById(reqBody.getRoleId());
        User userModel = userService.getUserById(userId);
        userModel.setName(reqBody.getName().toUpperCase());
        userModel.setUsername(reqBody.getUsername());
        userModel.setEmail(reqBody.getEmail());
        userModel.setPhone(reqBody.getPhone());
        userModel.setAddress(reqBody.getAddress().toUpperCase());
        userModel.setRole(role);
        User updatedUser = userService.updateUser(userModel);

        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.CREATE, SystemModels.USER, String.valueOf(updatedUser.getId()),
                updatedUser.getName(), gson.toJson(reqBody), request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);

        Map data = new HashMap();
        data.put("user", updatedUser);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @DeleteMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse deleteUser(@RequestHeader(name = "Authorization", required = true) String token,
                               @PathVariable(name = "userId") Integer userId,
                               HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.UserDelete, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        User user = userService.getUserById(userId);
        userService.deleleUser(user);

        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.DELETE, SystemModels.USER, String.valueOf(user.getId()),
                user.getName(), null, request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);

        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, null);
    }

    @GetMapping(value = "/users/{userId}/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse activateUser(@RequestHeader(name = "Authorization", required = true) String token,
                               @PathVariable(name = "userId") Integer userId,
                               HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.UserManage, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        User user = userService.getUserById(userId);
        user.setStatus("Y");
        User updatedUser = userService.updateUser(user);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.ACTIVATE, SystemModels.USER, String.valueOf(user.getId()),
                user.getName(), null, request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);

        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, null);
    }

    @GetMapping(value = "/users/{userId}/deactivate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse deactivateUser(@RequestHeader(name = "Authorization", required = true) String token,
                                 @PathVariable(name = "userId") Integer userId,
                                 HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.UserManage, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        User user = userService.getUserById(userId);
        user.setStatus("N");
        User updatedUser = userService.updateUser(user);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.DEACTIVATE, SystemModels.USER, String.valueOf(user.getId()),
                user.getName(), null, request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);

        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, null);
    }


    //========================User Auth EndPoints===================

    @Transactional
    @PostMapping(value = "/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse loginUser(@Valid @RequestBody(required = true) LoginRequest req,
                              HttpServletRequest httpServletRequest) {
        logger.info("================REQBODY: " + req.toString());
        logger.info("=======In Login Controller=========");
        LoginHistory history = new LoginHistory();
        history.setSourceIp(httpServletRequest.getRemoteAddr());
        history.setDestinationIp(httpServletRequest.getLocalAddr());
        history.setStatus("N");
        Optional<User> user = userRepository.findUserByUsernameIgnoreCase(req.getUsername());
        User userVo = new User();
        Map myData = new HashMap();
        if(!user.isPresent()) {
            logger.info("User Not Present");
            LoginHistory loginHistory = loginHistoryService.createLoginHistory(history); //save login history
            return new GeneralResponse(CommonResponse.LOGIN_FAILED_CODE, CommonResponse.LOGIN_FAILED_MSG);
        } else {
            User userModel = userService.getUserById(user.get().getId());
            history.setUser(user.get());
            if(user.get().getFailedLoginCount() >= configParams.getFailedLoginCount()
                    || !user.get().getStatus().equals("Y")) {
                logger.info("User Is disabled / Inactive");
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
                    logger.info("User Login Successful");
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
