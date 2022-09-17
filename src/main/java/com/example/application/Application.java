package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */

//TODO There is a spring-boot-starter-web dependency instead of webflux one,
// so need to investigate how move app to netty and webflux fully

@SpringBootApplication
@Theme(value = "hilla-chat")
@PWA(name = "hilla-chat", shortName = "hilla-chat", offlineResources = {})
@EnableReactiveMongoRepositories
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
