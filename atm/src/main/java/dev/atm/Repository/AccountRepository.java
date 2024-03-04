package dev.atm.Repository;

import dev.atm.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByLoginAndPin(String login, int pin);


}
