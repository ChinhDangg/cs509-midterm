package dev.atm.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Entity
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
}