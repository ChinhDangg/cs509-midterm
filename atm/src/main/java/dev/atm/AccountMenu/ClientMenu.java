package dev.atm.AccountMenu;

import dev.atm.AccountService;
import dev.atm.Entity.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class ClientMenu {
    private final AccountService accountService;
    public ClientMenu(AccountService accountService) {
        this.accountService = accountService;
    }

    public void runMenu(Scanner scanner, int id) {
        boolean running = true;
        while (running) {
            int option = getOption(scanner);
            if (option == 1)
                running = withdrawCash(scanner, id);
            else if (option == 2)
                running = depositCash(scanner, id);
            else if (option == 3)
                displayAccountBalance(id);
            else
                running = false;
        }
    }

    private void displayAccountBalance(int id) {
        System.out.println();
        Account acc = accountService.getAccountById(id);
        System.out.println("Account #" + id);
        System.out.println("Date: " + LocalDate.now());
        System.out.println("Balance: " + acc.getBalance());
    }

    private boolean depositCash(Scanner scanner, int id) {
        System.out.println();
        String amount = getDepositAmount(scanner);
        if (amount == null)
            return false;
        BigDecimal newBalance = accountService.setBalance(id, amount, true);
        if (newBalance != null) {
            System.out.println("Cash Deposited Successfully");
            System.out.println("Account #" + id);
            System.out.println("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            System.out.println("Deposited: " + amount);
            System.out.println("Balance: " + newBalance);
            return true;
        }
        System.out.println("Depositing error with server. Please try again");
        return false;
    }

    private String getDepositAmount(Scanner scanner) {
        int attempt = 5;
        while (attempt > 0) {
            System.out.print("Enter the cash amount to deposit: ");
            String amount = scanner.nextLine();
            if (amount.matches("\\d{1,8}(\\.\\d{1,2})?")) {
                BigDecimal am = new BigDecimal(amount);
                if (am.compareTo(new BigDecimal(0)) >= 0)
                    return amount;
                System.out.println("Please enter a positive amount");
            }
            else
                System.out.println("Please enter the amount in the correct format");
            attempt--;
        }
        return null;
    }

    private boolean withdrawCash(Scanner scanner, int id) { // make sure id exist otherwise error
        System.out.println();
        Account acc = accountService.getAccountById(id);
        String amount = getWithdrawAmount(scanner, acc.getBalance());
        if (amount == null)
            return false;
        BigDecimal newBalance = accountService.setBalance(id, amount, false);
        if (newBalance != null) {
            System.out.println("Cash Successfully Withdrawn");
            System.out.println("Account #" + acc.getId());
            System.out.println("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            System.out.println("Withdrawn: " + amount);
            System.out.println("Balance: " + newBalance);
            return true;
        }
        System.out.println("Withdrawing error with server. Please try again");
        return false;
    }

    private String getWithdrawAmount(Scanner scanner, BigDecimal balance) {
        int attempt = 5;
        while (attempt > 0) {
            System.out.print("Enter the withdrawal amount: ");
            String amount = scanner.nextLine();
            if (amount.matches("\\d{1,8}(\\.\\d{1,2})?")) {
                if (balance.compareTo(new BigDecimal(amount)) >= 0)
                    return amount;
                System.out.println("Your balance is insufficient for the withdraw");
            }
            else
                System.out.println("Please enter the amount in the correct format");
            attempt--;
        }
        System.out.println("Too many fail attempts");
        return null;
    }

    private int getOption(Scanner scanner) {
        System.out.println();
        int attempt = 5;
        while (attempt > 0) {
            System.out.println("1----Withdraw Cash");
            System.out.println("2----Deposit Cash");
            System.out.println("3----Display Balance");
            System.out.println("4----Exit");
            System.out.print("Select one option: ");
            String selectedOption = scanner.nextLine();
            if (selectedOption.matches("\\d+")) {
                int option = Integer.parseInt(selectedOption);
                if (option > 0 && option < 5)
                    return option;
            }
            System.out.println("Please enter a valid option");
            attempt--;
        }
        System.out.println("Too many fail attempt");
        return -1;
    }

}