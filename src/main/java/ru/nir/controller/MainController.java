package ru.nir.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nir.dao.TaskDTO;
import ru.nir.model.Task;
import ru.nir.service.MainService;

@RestController
@RequestMapping(value = "/api")
public class MainController {

    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/getTask/{id}")
    public Task getTask(@PathVariable("id") Long id) {
        return mainService.getTask(id);
    }

    @GetMapping("/allTask")
    public List<Task> showAllTasks() {
        return mainService.showAllTask();
    }

    @PostMapping("/addTask")
    public Task addTask(@RequestBody TaskDTO taskDTO){
        return mainService.addTask(taskDTO);
    }
}
