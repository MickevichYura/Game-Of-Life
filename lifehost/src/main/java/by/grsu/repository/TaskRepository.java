package by.grsu.repository;

import by.grsu.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long>  {
}
