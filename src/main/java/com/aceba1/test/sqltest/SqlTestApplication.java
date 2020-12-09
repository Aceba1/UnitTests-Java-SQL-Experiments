package com.aceba1.test.sqltest;

import com.aceba1.test.sqltest.service.PostgreSQLManagerService;
import com.aceba1.test.sqltest.utils.SQLProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class SqlTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqlTestApplication.class, args);

		Scanner scanner = new Scanner(System.in);
		while (true) {
			switch(scanner.next().toLowerCase()) {
				case "primitivecollect" -> {
					try { SQLProcessor.collect(PostgreSQLManagerService.getInstance().getConnection()); }
					catch (Exception e) { e.printStackTrace(); }
				}
				default -> System.err.println("Invalid entry");
			}
		}
	}

}
