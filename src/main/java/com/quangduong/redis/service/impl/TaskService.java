package com.quangduong.redis.service.impl;

import com.quangduong.redis.dto.todo.TaskDTO;
import com.quangduong.redis.dto.todo.TaskUpdateDTO;
import com.quangduong.redis.entity.TaskEntity;
import com.quangduong.redis.exception.NoPermissionException;
import com.quangduong.redis.mapper.TaskMapper;
import com.quangduong.redis.repository.TaskRepository;
import com.quangduong.redis.repository.UserRepository;
import com.quangduong.redis.service.ITaskService;
import com.quangduong.redis.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableCaching
public class TaskService implements ITaskService {

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    @Cacheable(value = "tasks", key = "@securityUtils.getUserId()") // generate key in redis with template tasks::userId
    public List<TaskDTO> getAllTasks() {
        long userId = securityUtils.getUserId();
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Not found user with id: " + userId))
                .getTasks().stream().map(taskMapper::toDTO).toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "tasks", key = "@securityUtils.getUserId()")
    public TaskDTO createTask(TaskDTO dto) {
        return taskMapper.toDTO(taskRepository.save(taskMapper.toEntity(dto)));
    }

    @Override
    @Transactional
    @CacheEvict(value = "tasks", key = "@securityUtils.getUserId()")
    public TaskDTO updateTask(TaskUpdateDTO dto) {
        TaskEntity entity = taskRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Not found todo with id: " + dto.getId()));
        if (entity.getUser().getId() != securityUtils.getUserId())
            throw new NoPermissionException("Not allowed");
        return taskMapper.toDTO(taskRepository.save(taskMapper.toEntity(entity, dto)));
    }

    @Override
    @Transactional
    @CacheEvict(value = "tasks", key = "@securityUtils.getUserId()")
    public void deleteTask(long id) {
        TaskEntity entity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found todo with id: " + id));
        if (entity.getUser().getId() != securityUtils.getUserId())
            throw new NoPermissionException("Not allowed");
        taskRepository.deleteById(id);
    }

    public TaskService() {}

    public TaskService(SecurityUtils securityUtils, UserRepository userRepository, TaskRepository taskRepository, TaskMapper taskMapper) {
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }
}
