package ru.nir.repository.fakeRepo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import ru.nir.model.Task;

@Repository
public class FakeRepository {

    List<Task> repository = new ArrayList<>();

    public Task addInRepoTask(Task task) {
        repository.add(task);
        return task;
    }

    public List<Task> showAllTasks() {
        return repository;
    }

    public Task getTask(Long id) {
        return repository.stream()
            .filter(element -> element.getId().equals(id))
            .findFirst()
            .orElseThrow();
    }

}
