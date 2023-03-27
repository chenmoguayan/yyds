package org.hj.yygh.dict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hj
 * @data 2023/3/23 15:07
 */
@SpringBootApplication
@ComponentScan("org.hj")
@EnableDiscoveryClient
public class ServiceDictMainApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ServiceDictMainApp.class, args);
    }
}
