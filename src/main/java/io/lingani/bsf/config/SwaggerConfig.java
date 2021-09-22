package io.lingani.bsf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.lingani.bsf.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "BSF Digital Banking Backend REST API",
                "Backend Service that exposes 2 REST APIs" +
                        "1. To accept money transfers to other accounts. Money transfers should persist new balance of accounts" +
                        "2. For creating an account and getting the account details. It disregards currencies at this time",
                "Version 1.0",
                "Terms of service",
                new Contact("Lingani", "-", "tshumal@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
