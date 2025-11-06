package taskmanagement;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface  AccountRepository  extends CrudRepository<Account, Long> {

    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);
}