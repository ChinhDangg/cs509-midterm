package dev.atm.Repository;

import dev.atm.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByLoginAndPin(String login, int pin);

    @Modifying
    @Query(value = "UPDATE Account SET " +
            "Login = :login, " +
            "Pin = :pin, " +
            "HoldersName = :holdersName, " +
            "Status = :status, " +
            "Role = :role " +
            "WHERE Id = :id", nativeQuery = true)
    void updateAccountBy(@Param("login") String login,
                               @Param("pin") int pin,
                               @Param("holdersName") String holdersName,
                               @Param("status") boolean status,
                               @Param("role") boolean role,
                               @Param("id") int id);

}
