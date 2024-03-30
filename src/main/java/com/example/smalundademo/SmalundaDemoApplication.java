package com.example.smalundademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SmalundaDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmalundaDemoApplication.class, args);
	}



	@RestController
	@RequestMapping("/api")
	public class Controller {
		@GetMapping("/string")
		public String getString(){
			return "foo";
		}
	}
}
