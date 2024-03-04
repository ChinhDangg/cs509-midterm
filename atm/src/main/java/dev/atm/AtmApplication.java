package dev.atm;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class AtmApplication {
	public static void main(String[] args) {
		SpringApplication.run(AtmApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ClientMenu clientMenu) {
		return args -> {
			clientMenu.runMenu();
		};
	}

}
