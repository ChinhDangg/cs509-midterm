package dev.atm;

import dev.atm.AccountMenu.AdminMenu;
import dev.atm.AccountMenu.ClientMenu;
import dev.atm.Entity.Account;
import dev.atm.Enum.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class AtmApplication {
	public static void main(String[] args) {
		SpringApplication.run(AtmApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(AccountService accountService) {
		return args -> {
			Scanner scanner = new Scanner(System.in);
			while (true) {
				String[] loginInfo = getLoginInfo(scanner);
				if (loginInfo == null)
					continue;
				Account acc = accountService.checkLogin(loginInfo[0], loginInfo[1]);
				if (!checkLogin(acc))
					continue;

				if (acc.getCurrentRole() == Role.Admin) {
					AdminMenu a = new AdminMenu(accountService);
					a.runMenu(scanner);
				}
				else {
					ClientMenu c = new ClientMenu(accountService);
					c.runMenu(scanner, acc.getId());
				}
			}
		};
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

}
