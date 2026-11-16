package com.library.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LibraryManagementSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				LibraryManagementSystemApplication.class, args);

		System.out.println("\u001B[34mStarted... LibraryManagementSystemApplication\u001B[0m");
		System.out.println("started............");
	}
}