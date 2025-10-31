package com.recipewiz.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.recipewiz.backend",
        "data_access",
        "use_case",
        "entity"
})
public class RecipeWizApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeWizApplication.class, args);
    }
}
