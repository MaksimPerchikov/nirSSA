package ru.nir.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nir.dao.TaskDTO;
import ru.nir.model.Task;
import ru.nir.repository.HandlerTaskOnRepository;
import ru.nir.service.interfaces.TaskMethodsForMainService;

@Service
public class MainService implements TaskMethodsForMainService {

    private final HandlerTaskOnRepository handlerTaskOnRepository;

    @Autowired
    public MainService(HandlerTaskOnRepository handlerTaskOnRepository) {
        this.handlerTaskOnRepository = handlerTaskOnRepository;
    }

    @Override
    public Task getTask(Long id) {
        return handlerTaskOnRepository.getTaskById(id);
    }

    @Override
    public List<Task> showAllTask() {
        return handlerTaskOnRepository.showAllTask();
    }

    @Override
    public Task addTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDesc());

        long sizeRepo = handlerTaskOnRepository.showAllTask().size();
        task.setId(sizeRepo+1);

        return handlerTaskOnRepository.addTask(task);
    }

    @Override
    public String deleteTask(Long id) {
         handlerTaskOnRepository.deleteTaskById(id);
         return null;//сделать сначала поиск, существует ли запись с таким id после удалить и отпарвить, что все гуд
    }

    @Override
    public Task updateTask(Task task) {
        return null;
    }
}
