package br.com.alura.metricforum;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class MetricForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetricForumApplication.class, args);
	}
}
