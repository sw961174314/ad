package com.ad.service.impl;

import com.ad.constant.Constants;
import com.ad.dao.AdUserRepository;
import com.ad.entity.AdUser;
import com.ad.exception.AdException;
import com.ad.service.IUserService;
import com.ad.util.CommonUtils;
import com.ad.vo.AdUser.CreateUserRequest;
import com.ad.vo.AdUser.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务接口实现
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private AdUserRepository userRepository;

    /**
     * 用户创建
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {
        // 判断用户名是否为空
        if (!request.validate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        // 判断当前用户名是否存在
        AdUser oldUser = userRepository.findByUsername(request.getUsername());
        if (oldUser != null) {
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }

        // 根据传入的request 创建token并创建用户保存到数据库中
        AdUser newUser = userRepository.save(new AdUser(request.getUsername(), CommonUtils.md5(request.getUsername())));

        return new CreateUserResponse(newUser.getId(), newUser.getUsername(), newUser.getToken(), newUser.getCreateTime(), newUser.getUpdateTime());
    }
}
