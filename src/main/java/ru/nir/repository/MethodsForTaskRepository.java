package ru.nir.repository;

import java.util.List;
import ru.nir.model.Task;

/*
    Методы для HandlerTaskOnRepository
 */

public interface MethodsForTaskRepository {

    Task getTaskById(Long id);

    List<Task> showAllTask();

    Task addTask(Task task);

    void deleteTaskById(Long id);

    Task updateTask(Task task);
}
