package com.quangduong.redis.service;

import com.quangduong.redis.dto.user.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO dto);
}
