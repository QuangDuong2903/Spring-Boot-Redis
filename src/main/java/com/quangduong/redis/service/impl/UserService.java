package com.quangduong.redis.service.impl;

import com.quangduong.redis.dto.user.UserDTO;
import com.quangduong.redis.entity.UserEntity;
import com.quangduong.redis.exception.UsernameExistsException;
import com.quangduong.redis.mapper.UserMapper;
import com.quangduong.redis.repository.UserRepository;
import com.quangduong.redis.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO dto) {
        Optional<UserEntity> user = userRepository.findOneByUsername(dto.getUsername());
        if (user.isPresent())
            throw new UsernameExistsException("Username already exists");
        return userMapper.toDTO(userRepository.save(userMapper.toEntity(dto)));
    }
}
