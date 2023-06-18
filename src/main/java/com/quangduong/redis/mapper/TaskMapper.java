package com.quangduong.redis.mapper;

import com.quangduong.redis.dto.todo.TaskDTO;
import com.quangduong.redis.dto.todo.TaskUpdateDTO;
import com.quangduong.redis.entity.TaskEntity;
import com.quangduong.redis.repository.UserRepository;
import com.quangduong.redis.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserRepository userRepository;

    public TaskEntity toEntity(TaskDTO dto) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(dto.getTitle());
        entity.setDone(dto.getDone());
        long userId = securityUtils.getUserId();
        entity.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Not found user with id: " + userId)));
        return entity;
    }

    public TaskEntity toEntity(TaskEntity entity, TaskUpdateDTO dto) {
        if (dto.getTitle() != null)
            entity.setTitle(dto.getTitle());
        if (dto.getDone() != null)
            entity.setDone(dto.getDone());
        return entity;
    }

    public TaskDTO toDTO(TaskEntity entity) {
        TaskDTO dto = new TaskDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDone(entity.isDone());
        return dto;
    }
}
