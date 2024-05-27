package com.ad.service;

import com.ad.exception.AdException;
import com.ad.vo.AdUser.CreateUserRequest;
import com.ad.vo.AdUser.CreateUserResponse;

/**
 * 用户账号服务功能
 */
public interface IUserService {

    // 用户创建
    CreateUserResponse createUser(CreateUserRequest request) throws AdException;
}