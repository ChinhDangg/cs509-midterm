package dev.atm.AccountMenu;

import dev.atm.AccountService;
import dev.atm.Entity.Account;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AdminMenu {
    private final AccountService accountService;

    public AdminMenu(AccountService accountService) {
        this.accountService = accountService;
    }

    public void runMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            int option = getOption(scanner);
            if (option == 1)
                running = createNewAccount(scanner);
            else if (option == 2)
                running = deleteExistingAccount(scanner);
            else if (option == 3)
                running = updateAccount(scanner);
            else if (option == 4)
                running = searchForAccount(scanner);
            else
                running = false;
        }
    }

    private boolean searchForAccount(Scanner scanner) {
        System.out.println();
        Account acc = getExistAccountWithInputId(scanner);
        if (acc != null) {
            System.out.println("The account information is:");
            displayAccountInfo(acc);
            return true;
        }
        return false;
    }

    private void displayAccountInfo(Account acc) { // doesn't go alone, need to get the latest account first
        System.out.println();
        System.out.println("Account # "+acc.getId());
        System.out.println("Holder: "+acc.getHoldersName());
        System.out.println("Balance: "+acc.getBalance());
        System.out.println("Status: "+acc.getCurrentStatus());
        System.out.println("Login: "+acc.getLogin());
        System.out.println("Pin: "+acc.getPin());
    }

    private Account getExistAccountWithInputId(Scanner scanner) {
        int attempt = 5;
        while (attempt > 0) {
            int id = getAccountId(scanner, ":");
            if (id == -1)
                return null;
            Account acc = accountService.getAccountById(id);
            if (acc != null)
                return acc;
            System.out.println("Account doesn't exist. Try again");
            attempt--;
        }
        System.out.println("Too many fail attempts");
        return null;
    }

    private boolean updateAccount(Scanner scanner) {
        System.out.println();
        Account previousAcc = getExistAccountWithInputId(scanner);
        if (previousAcc != null) {
            Account updateAccount = getAccountDetails(scanner, false);
            if (updateAccount != null) {
                int accId = accountService.updateAccount(updateAccount, previousAcc.getId());
                if (accId == -1)
                    System.out.println("Update account error with server? Try again");
                else
                    System.out.println("Account Updated Successfully");
                return accId != -1;
            }
        }
        return false;
    }

    private boolean deleteExistingAccount(Scanner scanner) {
        System.out.println();
        int attempt = 5;
        while (attempt > 0) {
            int id = getAccountId(scanner, " to which you want to delete:");
            if (id != -1) {
                Account acc = accountService.getAccountById(id);
                if (acc != null) {
                    System.out.print("You wish to delete the account held by " + acc.getHoldersName() +
                            ". If this information is correct, please re-enter the account number: ");
                    String reId = scanner.nextLine();
                    if (reId.equals(String.valueOf(id))) {
                        boolean deleteStatus = accountService.deleteAccount(id);
                        if (!deleteStatus)
                            System.out.println("Delete operation failed with server? Try again");
                        else
                            System.out.println("Account Deleted Successfully");
                        return deleteStatus;
                    }
                    System.out.println("Delete id mismatched. Try again");
                }
            }
            else
                return false;
            attempt--;
        }
        System.out.println("Too many fail attempts");
        return false;
    }

    private int getAccountId(Scanner scanner, String print) {
        int attempt = 5;
        while (attempt > 0) {
            System.out.print("Enter the account number" + print + " ");
            String id = scanner.nextLine();
            if (id.matches("\\d+"))
                return Integer.parseInt(id);
            System.out.println("Please enter the account number is the correct format");
            attempt--;
        }
        System.out.println("Too many fail attempts");
        return -1;
    }


    private boolean createNewAccount(Scanner scanner) {
        System.out.println();
        Account acc = getAccountDetails(scanner, true);
        if (acc != null) {
            int accId = accountService.addNewAccount(acc);
            System.out.println();
            if (accId != -1) {
                System.out.println("Account Successfully Created - the account number assigned is: " + accId);
                return true;
            }
            System.out.println("Unable to create account with server? Please try again");
        }
        return false;
    }

    private Account getAccountDetails(Scanner scanner, boolean getBalance) {
        int attempt = 5;
        String balance = "";
        while (attempt > 0) {
            System.out.print("Login: ");
            String login = scanner.nextLine();
            System.out.print("Pin Code: ");
            String pin = scanner.nextLine();
            System.out.print("Holders Name: ");
            String holdersName = scanner.nextLine();
            if (getBalance) {
                System.out.print("Starting Balance: ");
                balance = scanner.nextLine();
            }
            System.out.print("Status: ");
            String status = scanner.nextLine().toLowerCase();

            if (login.isEmpty())
                System.out.println("Please enter the login");
            else if (pin.isEmpty())
                System.out.println("Please enter the pin");
            else if (holdersName.isEmpty())
                System.out.println("Please enter the holders name");
            else if (getBalance && balance.isEmpty())
                System.out.println("Please enter the starting balance");
            else if (status.isEmpty())
                System.out.println("Please enter the status");
            else if (!pin.matches("\\d+"))
                System.out.println("Please enter the pin in the correct format");
            else if (getBalance && !balance.matches("\\d+"))
                System.out.println("Please enter the balance in the correct format");
            else if (!status.equals("disabled") && !status.equals("active"))
                System.out.println("Please enter the status in the correct format");
            else {
                if (getBalance)
                    return new Account(login, Integer.parseInt(pin), holdersName, new BigDecimal(balance), getStatus(status), false);
                return new Account(login, Integer.parseInt(pin), holdersName, getStatus(status), false);
            }
            attempt--;
        }
        System.out.println("Too many fail attempts");
        return null;
    }

    private int getOption(Scanner scanner) {
        System.out.println();
        int attempt = 5;
        while (attempt > 0) {
            System.out.println("1----Create New Account");
            System.out.println("2----Delete Existing Account");
            System.out.println("3----Update Account Information");
            System.out.println("4----Search for Account");
            System.out.println("5----Exit");
            System.out.print("Select one option: ");
            String selectedOption = scanner.nextLine();
            if (selectedOption.matches("\\d+")) {
                int option = Integer.parseInt(selectedOption);
                if (option > 0 && option < 6)
                    return option;
            }
            System.out.println("Please enter a valid option");
            attempt--;
        }
        System.out.println("Too many fail attempt");
        return -1;
    }

    private boolean getStatus(String status) {
        return (status.equalsIgnoreCase("active"));
    }


}
