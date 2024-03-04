package dev.atm.Entity;

import dev.atm.Enum.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "Login", nullable = false)
    private String login;
    @Column(name = "Pin", nullable = false)
    private int pin;
    @Column(name = "HoldersName", nullable = false)
    private String holdersName;
    @Column(name = "Balance", precision = 10, scale = 2, nullable = false)
    private BigDecimal balance;
    @Column(name = "Status", nullable = false)
    private boolean status;
    @Column(name = "Role", nullable = false)
    private boolean role;

    public Account(String login, int pin, String holdersName, BigDecimal balance, boolean status, boolean role) {
        this.login = login;
        this.pin = pin;
        this.holdersName = holdersName;
        this.balance = balance;
        this.status = status;
        this.role = role;
    }

    public Account(String login, int pin, String holdersName, boolean status, boolean role) {
        this.login = login;
        this.pin = pin;
        this.holdersName = holdersName;
        this.status = status;
        this.role = role;
    }

    public Role getCurrentRole() {
        return (role) ? Role.Admin : Role.Client;
    }

    public String getCurrentStatus() {
        return (status) ? "Active" : "Disabled";
    }
}