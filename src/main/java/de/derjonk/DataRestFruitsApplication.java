package de.derjonk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class DataRestFruitsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataRestFruitsApplication.class, args);
	}
}
