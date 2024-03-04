package dev.atm;

import dev.atm.Entity.Account;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

@Component
public class ClientMenu {
    private final AccountService accountService;
    public ClientMenu(AccountService accountService) {
        this.accountService = accountService;
    }

    public void runMenu() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String[] loginInfo = getLoginInfo(scanner);
            if (loginInfo == null)
                continue;
            Account acc = accountService.checkLogin(loginInfo[0], loginInfo[1]);
            if (!checkLogin(acc))
                continue;

            boolean running = true;
            while (running) {
                int option = getOptions(scanner);
                if (option == 1)
                    running = withdrawCash(scanner, acc.getId());
                else if (option == 2)
                    running = depositCash(scanner, acc.getId());
                else if (option == 3)
                    displayAccountBalance(acc.getId());
                else
                    running = false;
            }
        }
    }

    private boolean checkLogin(Account account) {
        if (account == null) {
            System.out.println("Wrong login or pin");
            return false;
        }
        else if (!account.isStatus()) {
            System.out.println("Your account is currently disabled, please contact your bank");
            return false;
        }
        return true;
    }

    private String[] getLoginInfo(Scanner scanner) {
        System.out.println();
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        System.out.print("Enter Pin code: ");
        String pin = scanner.nextLine();
        if (login.isEmpty()) {
            System.out.println("Please enter your login");
            return null;
        }
        else if (pin.isEmpty()) {
            System.out.println("Please enter your pin");
            return null;
        }
        else if (!pin.matches("\\d+")) {
            System.out.println("Please enter your pin in correct format");
            return null;
        }
        return new String[]{ login, pin };
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
            System.out.println("Date: " + LocalDate.now());
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
            System.out.println("Date: " + LocalDate.now());
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

    private int getOptions(Scanner scanner) {
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