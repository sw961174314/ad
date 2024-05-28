package com.ad.controller;

import com.ad.exception.AdException;
import com.ad.service.IUserService;
import com.ad.vo.AdUser.CreateUserRequest;
import com.ad.vo.AdUser.CreateUserResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 */
@Slf4j
@RestController
public class UserOpController {

    @Autowired
    private IUserService userService;

    /**
     * 用户创建
     * @param request
     * @return
     * @throws AdException
     */
    @PostMapping("/create/user")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) throws AdException {
        log.info("ad-sponsor: createUser -> {}", JSON.toJSONString(request));
        return userService.createUser(request);
    }
}