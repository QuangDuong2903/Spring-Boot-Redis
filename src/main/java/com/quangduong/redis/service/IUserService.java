package com.quangduong.redis.service;

import com.quangduong.redis.dto.user.UserDTO;

public interface IUserService {

    UserDTO createUser(UserDTO dto);
}
