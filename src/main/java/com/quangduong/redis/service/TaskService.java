package com.quangduong.redis.service;

import com.quangduong.redis.dto.todo.TaskDTO;
import com.quangduong.redis.dto.todo.TaskUpdateDTO;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAllTasks();

    TaskDTO createTask(TaskDTO dto);

    TaskDTO updateTask(TaskUpdateDTO dto);

    void deleteTask(long id);
}
