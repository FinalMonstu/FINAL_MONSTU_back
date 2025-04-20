package com.icetea.MonStu;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
public class MonStuApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonStuApplication.class, args);
	}

}
