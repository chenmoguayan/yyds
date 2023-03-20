package org.hj.yygh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hj
 * @data 2023/3/20 15:32
 */
@SpringBootApplication
@ComponentScan("org.hj")
public class ServiceHospitalMainApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ServiceHospitalMainApp.class, args);
    }
}
