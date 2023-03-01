package com.dreamx.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.stream.Collectors;

@SpringBootApplication
public class TasksApplication {

	private static final Logger logger = LogManager.getLogger(TasksApplication.class);

	public static void main(String[] args) {

		var applicationContext = SpringApplication.run(TasksApplication.class, args);

		String apis = applicationContext.getBeansOfType(RouterFunction.class)
				.values()
				.stream()
				.map(Object::toString)
				.collect(Collectors.joining("/n"));

		logger.info("APIs: {}", apis);


	}

}
