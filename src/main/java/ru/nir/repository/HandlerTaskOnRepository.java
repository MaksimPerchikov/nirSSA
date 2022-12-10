package ru.nir.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nir.model.Task;
import ru.nir.repository.fakeRepo.FakeRepository;

/*
 Основной класс для взаимодействия с репозиторием
 */

@Component
public class HandlerTaskOnRepository implements MethodsForTaskRepository {

    //private final TaskRepository taskRepository;
    private final FakeRepository fakeRepository;

    @Autowired
    public HandlerTaskOnRepository(/*TaskRepository taskRepository*/FakeRepository fakeRepository) {
     //   this.taskRepository = taskRepository;
        this.fakeRepository = fakeRepository;
    }


    @Override
    public Task getTaskById(Long id) {
       // return taskRepository.getById(id);
        return fakeRepository.getTask(id);
    }

    @Override
    public List<Task> showAllTask() {
        //return taskRepository.findAll();
        return fakeRepository.showAllTasks();
    }

    @Override
    public Task addTask(Task task) {
        //return taskRepository.save(task);
        return fakeRepository.addInRepoTask(task);
    }

    @Override
    public void deleteTaskById(Long id) {
       // taskRepository.deleteById(id);
    }

    @Override
    public Task updateTask(Task task) {
        return null;
        //сделать
    }
}
