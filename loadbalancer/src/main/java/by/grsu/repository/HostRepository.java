package by.grsu.repository;

import by.grsu.model.Host;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HostRepository extends CrudRepository<Host, Long> {
    List<Host> findByIp(String lastName);
}
