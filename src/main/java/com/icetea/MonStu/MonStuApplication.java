package com.icetea.MonStu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonStuApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonStuApplication.class, args);
	}

}
