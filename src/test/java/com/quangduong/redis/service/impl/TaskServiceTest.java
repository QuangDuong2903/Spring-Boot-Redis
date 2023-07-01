package com.quangduong.redis.service.impl;

import com.quangduong.redis.dto.todo.TaskDTO;
import com.quangduong.redis.entity.TaskEntity;
import com.quangduong.redis.entity.UserEntity;
import com.quangduong.redis.exception.ResourceNotFoundException;
import com.quangduong.redis.mapper.TaskMapper;
import com.quangduong.redis.repository.TaskRepository;
import com.quangduong.redis.repository.UserRepository;
import com.quangduong.redis.utils.SecurityUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    private TaskService taskService;

    @BeforeEach
    public void setup() {
        taskService = new TaskService(securityUtils, userRepository, taskRepository, taskMapper);
    }

    @Test
    @DisplayName("Should throw not found user")
    void getAllTasksShouldThrowNotFoundUser() {

        Mockito.when(securityUtils.getUserId()).thenReturn(1L);
        Mockito.when(userRepository.findById(1L)).thenThrow(new ResourceNotFoundException("Not found user with id: 1"));

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> taskService.getAllTasks())
                .withMessage("Not found user with id: 1");
    }

    @Test
    @DisplayName("Should return list of tasks")
    void getAllTasksShouldReturnListOfTasks() {

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setTasks(Arrays.asList(
                new TaskEntity("task test", true, user)
        ));

        TaskDTO dto = new TaskDTO(1, "task test", true);

        Mockito.when(securityUtils.getUserId()).thenReturn(1L);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(taskMapper.toDTO(Mockito.any(TaskEntity.class))).thenReturn(dto);

        List<TaskDTO> tasks = taskService.getAllTasks();
        Assertions.assertThat(tasks.size()).isEqualTo(1);
        Assertions.assertThat(tasks.get(0).getId()).isEqualTo(1);
        Assertions.assertThat(tasks.get(0).getTitle()).isEqualTo("task test");
        Assertions.assertThat(tasks.get(0).getDone()).isEqualTo(true);
    }

    @Test
    @DisplayName("Should save task")
    void createTaskShouldSaveTask() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        TaskEntity entity = new TaskEntity("task test", true, user);
        TaskDTO dto = new TaskDTO(1, "task test", true);

        Mockito.when(taskMapper.toEntity(Mockito.any(TaskDTO.class))).thenReturn(entity);
        Mockito.when(taskMapper.toDTO(Mockito.any(TaskEntity.class))).thenReturn(dto);
        Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class))).thenReturn(entity);

        taskService.createTask(dto);
        Mockito.verify(taskRepository, Mockito.times(1)).save(ArgumentMatchers.any(TaskEntity.class));
    }

}