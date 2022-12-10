package ru.nir.service.interfaces;

import java.util.List;
import ru.nir.dao.TaskDTO;
import ru.nir.model.Task;

public interface TaskMethodsForMainService {

    Task getTask(Long id);

    List<Task> showAllTask();

    Task addTask(TaskDTO taskDTO);

    String deleteTask(Long id);

    Task updateTask(Task task);

}
